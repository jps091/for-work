package project.forwork.api.domain.order.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.domain.order.controller.model.ConfirmRequest;
import project.forwork.api.domain.order.controller.model.PartialCancelRequest;
import project.forwork.api.domain.order.controller.model.PaymentFullCancelRequest;
import project.forwork.api.domain.order.controller.model.PaymentPartialCancelRequest;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.orderresume.model.OrderResume;
import project.forwork.api.domain.orderresume.service.port.OrderResumeRepository;
import project.forwork.api.domain.retrylog.service.RetryLogService;
import project.forwork.api.domain.transaction.controller.model.TransactionCreateRequest;
import project.forwork.api.domain.transaction.infrastructure.enums.TransactionType;
import project.forwork.api.domain.transaction.model.Transaction;
import project.forwork.api.domain.transaction.service.TransactionService;
import project.forwork.api.domain.retrylog.infrastructure.enums.*;

import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.util.List;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
/***
 * 결제 관려 로직 작성 -> 주문 서비스를 통한 주문 상태변경 -> API 통합
 */
public class CheckoutService {

    private final PaymentGatewayService pgService;
    private final TransactionService transactionService;
    private final OrderService orderService;
    private final OrderResumeRepository orderResumeRepository;
    private final RetryLogService retryLogService;


    public void processOrderAndPayment(CurrentUser currentUser, ConfirmRequest body){
        try{
            pgService.confirm(body);

        }catch (Exception e){
            log.error("caught process order-payment", e);


            if (e instanceof RestClientException && e.getCause() instanceof SocketTimeoutException) {
                retryLogService.register(body.getOrderId(), RetryType.CONFIRM, e);
            }

            // 결제 실패 시 주문 상태 업데이트
            orderService.updateOrderConfirmFailure(body);

            throw e;  // 예외를 다시 던져서 상위 로직에서 처리할 수 있게 함
        }

        Order order = orderService.updateOrderConfirm(body);

        createPgTransaction(currentUser, order, body.getPaymentKey(), new BigDecimal(body.getAmount()), TransactionType.PAYMENT);
    }

    public void cancelPayment(CurrentUser currentUser, Long orderId){
        Transaction transaction = transactionService.getByOrderIdAndUserId(orderId, currentUser.getId());
        PaymentFullCancelRequest body = PaymentFullCancelRequest.builder()
                .cancelReason("주문 전체 취소")
                .build();
        try{
            pgService.cancelFullPayment(transaction.getPaymentKey(), body);

        }catch (Exception e){
            log.error("caught process cancel-payment", e);

            if (e instanceof RestClientException && e.getCause() instanceof SocketTimeoutException) {
                retryLogService.register(transaction.getPaymentKey(), RetryType.CANCEL, e);
            }
            throw e;  // 예외를 다시 던져서 상위 로직에서 처리할 수 있게 함
        }

        Order order = orderService.getById(orderId);
        createPgTransaction(currentUser, order, transaction.getPaymentKey(), order.getTotalAmount(), TransactionType.REFUND);
    }

    public void cancelPartialPayment(CurrentUser currentUser, Long orderId, PartialCancelRequest body){
        Order order = orderService.getById(orderId);
        List<OrderResume> orderResumes = orderResumeRepository.findByIds(body.getOrderResumeIds());
        BigDecimal cancelAmount = order.getPartialCancelAmount(orderResumes);

        PaymentPartialCancelRequest paymentBody = PaymentPartialCancelRequest.builder()
                .cancelAmount(cancelAmount)
                .cancelReason("부분 주문 취소")
                .build();

        Transaction transaction = transactionService.getByOrderIdAndUserId(orderId, currentUser.getId());

        try{
            pgService.cancelPartialPayment(transaction.getPaymentKey(), paymentBody);

        }catch (Exception e){
            log.error("caught process partial-cancel-payment", e);

            if (e instanceof RestClientException && e.getCause() instanceof SocketTimeoutException) {
                retryLogService.register(transaction.getPaymentKey(), RetryType.PARTIAL_CANCEL, e);
            }
            throw e;  // 예외를 다시 던져서 상위 로직에서 처리할 수 있게 함
        }

        createPgTransaction(currentUser, order, transaction.getPaymentKey(), cancelAmount, TransactionType.REFUND);
    }

    public void createPgTransaction(
            CurrentUser currentUser, Order order, String paymentKey,
            BigDecimal amount, TransactionType type
    ){
        TransactionCreateRequest txBody = TransactionCreateRequest.builder()
                .userId(currentUser.getId())
                .orderId(order.getId())
                .paymentKey(paymentKey)
                .amount(amount)
                .type(type)
                .build();

        transactionService.createTransaction(txBody);
    }
}
