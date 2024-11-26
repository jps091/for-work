package project.forwork.api.domain.order.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.domain.order.controller.model.CancelRequest;
import project.forwork.api.domain.order.controller.model.CancelResponse;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.order.service.port.OrderRepository;
import project.forwork.api.domain.orderresume.model.OrderResume;
import project.forwork.api.domain.orderresume.service.OrderResumeService;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class CancelService {
    private final OrderResumeService orderResumeService;
    private final OrderRepository orderRepository;
    private final  CheckoutService checkoutService;
    @Transactional
    public CancelResponse cancel(CurrentUser currentUser, Long orderId, CancelRequest body){
        List<OrderResume> orderResumes = orderResumeService.getCancelRequestOrderResumes(body.getOrderResumeIds(), orderId);
        BigDecimal cancelAmount = getCancelAmount(orderResumes);
        Order order = orderRepository.getByIdWithThrow(orderId);

        if(order.isAllCancel(cancelAmount)){
            log.info("isAllCancel order={} cancel={}", order.getTotalAmount(), cancelAmount);
            checkoutService.cancelPayment(currentUser, order);
            return CancelResponse.fromAllCancel(order.getTotalAmount());
        }

        log.info("partCancel order={} cancel={}", order.getTotalAmount(), cancelAmount);
        checkoutService.cancelPartialPayment(currentUser, order, orderResumes);
        return CancelResponse.fromPartCancel(cancelAmount);
    }

    private static BigDecimal getCancelAmount(List<OrderResume> orderResumes) {
        return orderResumes.stream()
                .map(OrderResume::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
