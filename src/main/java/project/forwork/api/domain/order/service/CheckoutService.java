package project.forwork.api.domain.order.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientResponseException;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.common.error.TransactionErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.cartresume.service.CartResumeService;
import project.forwork.api.domain.order.controller.model.*;
import project.forwork.api.domain.order.infrastructure.enums.OrderStatus;
import project.forwork.api.domain.order.infrastructure.model.ConfirmPaymentDto;
import project.forwork.api.domain.order.infrastructure.model.PaymentFullCancelDto;
import project.forwork.api.domain.order.infrastructure.model.PaymentPartialCancelDto;
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
@Builder
@AllArgsConstructor
public class CheckoutService {

    private final PaymentGatewayService pgService;
    private final TransactionRepository transactionRepository;
    private final OrderService orderService;
    private final RetryLogService retryLogService;
    private final CartResumeService cartResumeService;

    @Transactional
    public ConfirmResponse processOrderAndPayment(CurrentUser currentUser, ConfirmPaymentRequest body){
        validRequestId(body); // 멱등성 확인 후 주문 생성
        try{
            // 결체 요청
            ConfirmPaymentDto confirmPaymentDto = ConfirmPaymentDto.from(body);
            pgService.confirm(confirmPaymentDto);

            Order order = orderService.create(currentUser, body);
            cartResumeService.deleteByPaidResumeIds(currentUser, body.getResumeIds());

            Transaction tx = Transaction.create(currentUser, body.getRequestId(), body.getPaymentKey(), body.getAmount(), TransactionType.PAYMENT);
            transactionRepository.save(tx);

            return ConfirmResponse.from(order, body.getResumeIds());
        }catch (Exception e){
            log.error("caught process order-payment", e);
            throwApiExceptionIfStatusCode500(e, body.getRequestId(), RetryType.CONFIRM);
            throw e;  // 예외를 다시 던져서 상위 로직에서 처리할 수 있게 함
        }
    }

    @Transactional
    public void cancelPayment(CurrentUser currentUser, Order order){
        String requestId = orderService.getRequestIdByOrderId(order.getId());

        try{
            PaymentFullCancelDto body = createCancelBody();
            Transaction transaction = transactionRepository.getByRequestIdAndEmail(requestId, currentUser.getEmail());

            pgService.cancelFullPayment(transaction.getPaymentKey(), body);
            orderService.cancelOrder(currentUser, order);

            Transaction tx = Transaction.create(currentUser, requestId, transaction.getPaymentKey(), order.getTotalAmount(), TransactionType.REFUND);
            transactionRepository.save(tx);
        }catch (Exception e){
            log.error("caught process cancel-payment", e);
            throwApiExceptionIfStatusCode500(e, requestId, RetryType.CANCEL);
            throw e;
        }
    }

    @Transactional
    public void cancelPartialPayment(CurrentUser currentUser, Order order, List<OrderResume> orderResumes){
        String requestId = orderService.getRequestIdByOrderId(order.getId());

        try{
            Transaction transaction = transactionRepository.getByRequestIdAndEmail(requestId, currentUser.getEmail());
            PaymentPartialCancelDto paymentBody = createPartialCancelBody(orderResumes);

            pgService.cancelPartialPayment(transaction.getPaymentKey(), paymentBody);
            orderService.cancelPartialOrder(currentUser, order, orderResumes);

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

    private static PaymentFullCancelDto createCancelBody() {
        return PaymentFullCancelDto.builder()
                .cancelReason("주문 전체 취소")
                .build();
    }

    private PaymentPartialCancelDto createPartialCancelBody(List<OrderResume> orderResumes){
        BigDecimal cancelAmount = orderResumes.stream()
                .map(OrderResume::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return PaymentPartialCancelDto.builder()
                .cancelAmount(cancelAmount)
                .cancelReason("부분 주문 취소")
                .build();
    }
}
