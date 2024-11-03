package project.forwork.api.domain.order.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientResponseException;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.common.error.TransactionErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.cartresume.service.CartResumeService;
import project.forwork.api.domain.order.controller.model.*;
import project.forwork.api.domain.order.infrastructure.model.ConfirmPaymentDto;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.orderresume.model.OrderResume;
import project.forwork.api.domain.orderresume.service.OrderResumeService;
import project.forwork.api.domain.retrylog.service.RetryLogService;
import project.forwork.api.domain.transaction.infrastructure.enums.TransactionType;
import project.forwork.api.domain.transaction.model.Transaction;
import project.forwork.api.domain.retrylog.infrastructure.enums.*;
import project.forwork.api.domain.transaction.service.port.TransactionRepository;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class CheckoutService {

    private final PaymentGatewayService pgService;
    private final TransactionRepository transactionRepository;
    private final OrderService orderService;
    private final OrderResumeService orderResumeService;
    private final RetryLogService retryLogService;
    private final CartResumeService cartResumeService;

    public Order processOrderAndPayment(CurrentUser currentUser, ConfirmPaymentRequest body){
        // 멱등성 확인 후 주문 생성
        validRequestId(body);
        Order order = orderService.create(currentUser, body);

        try{
            // 결체 요청
            ConfirmPaymentDto confirmPaymentDto = ConfirmPaymentDto.from(body);
            pgService.confirm(confirmPaymentDto);

            orderService.updateOrderPaid(order);
            cartResumeService.deleteByConfirmed(currentUser, body.getResumeIds());

            Transaction tx = Transaction.create(currentUser, body.getRequestId(), body.getPaymentKey(), body.getAmount(), TransactionType.PAYMENT);
            transactionRepository.save(tx);
            return order;
        }catch (Exception e){
            // 결제 실패 시 주문 상태 업데이트
            orderService.updateOrderConfirmFailure(order);

            log.error("caught process order-payment", e);
            throwApiExceptionIfStatusCode500(e, order.getRequestId(), RetryType.CONFIRM);
            throw e;  // 예외를 다시 던져서 상위 로직에서 처리할 수 있게 함
        }
    }

    public void cancelPayment(CurrentUser currentUser, Long orderId){
        String requestId = orderService.getRequestIdByOrderId(orderId);
        Transaction transaction = transactionRepository.getByRequestIdAndEmail(requestId, currentUser.getEmail());

        try{
            PaymentFullCancelRequest body = createCancelBody();
            pgService.cancelFullPayment(transaction.getPaymentKey(), body);
            Order order = orderService.cancelOrder(currentUser, orderId);

            Transaction tx = Transaction.create(currentUser, requestId, transaction.getPaymentKey(), order.getTotalAmount(), TransactionType.REFUND);
            transactionRepository.save(tx);

        }catch (Exception e){
            log.error("caught process cancel-payment", e);
            throwApiExceptionIfStatusCode500(e, requestId, RetryType.CANCEL);
            throw e;
        }
    }

    public void cancelPartialPayment(CurrentUser currentUser, Long orderId, PartialCancelRequest body){
        String requestId = orderService.getRequestIdByOrderId(orderId);
        Transaction transaction = transactionRepository.getByRequestIdAndEmail(requestId, currentUser.getEmail());

        try{
            List<OrderResume> orderResumes = orderResumeService.getCancelRequestOrderResumes(body.getOrderResumeIds(), orderId);
            PaymentPartialCancelRequest paymentBody = createPartialCancelBody(orderResumes);

            pgService.cancelPartialPayment(transaction.getPaymentKey(), paymentBody);
            orderService.cancelPartialOrder(currentUser, orderId, orderResumes);

            Transaction tx = Transaction.create(currentUser, requestId, transaction.getPaymentKey(), paymentBody.getCancelAmount(), TransactionType.PARTIAL_REFUND);
            transactionRepository.save(tx);

        }catch (Exception e){
            log.error("caught process partial-cancel-payment", e);

            throwApiExceptionIfStatusCode500(e, requestId, RetryType.PARTIAL_CANCEL);
            throw e;
        }
    }

    private void throwApiExceptionIfStatusCode500(Exception e, String requestId, RetryType confirm) {
        if (e instanceof RestClientResponseException restException) {
            if (restException.getStatusCode().value() == 500) {
                retryLogService.register(requestId, confirm, e);
                throw new ApiException(TransactionErrorCode.SERVER_ERROR);
            }
        }
    }

    private void validRequestId(ConfirmPaymentRequest body){
        orderService.validRequestId(body.getRequestId());
    }

    private static PaymentFullCancelRequest createCancelBody() {
        return PaymentFullCancelRequest.builder()
                .cancelReason("주문 전체 취소")
                .build();
    }

    private PaymentPartialCancelRequest createPartialCancelBody(List<OrderResume> orderResumes){
        BigDecimal cancelAmount = orderResumes.stream()
                .map(OrderResume::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return PaymentPartialCancelRequest.builder()
                .cancelAmount(cancelAmount)
                .cancelReason("부분 주문 취소")
                .build();
    }
}
