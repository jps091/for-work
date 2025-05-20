package project.forwork.api.domain.order.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.common.error.OrderResumeErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.order.controller.model.CancelRequest;
import project.forwork.api.domain.order.controller.model.CancelResponse;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.order.service.port.OrderQueryPort;
import project.forwork.api.domain.orderresume.infrastructure.enums.OrderResumeStatus;
import project.forwork.api.domain.orderresume.model.OrderResume;
import project.forwork.api.domain.orderresume.model.OrderResumes;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class CancelService {
    private final OrderQueryPort orderQueryPort;
    private final  CheckoutService checkoutService;

    @Transactional
    public CancelResponse cancel(CurrentUser currentUser, Long orderId, CancelRequest body){
        OrderResumes orderResumes = orderQueryPort.findByOrderIdAndStatus(body.getOrderResumeIds(), orderId, OrderResumeStatus.PAID);
        BigDecimal cancelAmount = orderResumes.calculateAmount();
        Order order = orderQueryPort.getByIdWithThrow(orderId);

        if(order.isAllCancel(cancelAmount)){
            log.info("isAllCancel order={} cancel={}", order.getTotalAmount(), cancelAmount);
            checkoutService.cancelPayment(currentUser, order);
            return CancelResponse.fromAllCancel(order.getTotalAmount());
        }

        log.info("partCancel order={} cancel={}", order.getTotalAmount(), cancelAmount);
        checkoutService.cancelPartialPayment(currentUser, order, orderResumes);
        return CancelResponse.fromPartCancel(cancelAmount);
    }
}
