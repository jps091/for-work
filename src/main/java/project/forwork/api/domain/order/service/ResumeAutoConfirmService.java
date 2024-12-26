package project.forwork.api.domain.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import project.forwork.api.domain.order.infrastructure.enums.OrderStatus;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.order.model.Orders;
import project.forwork.api.domain.order.service.port.OrderRepository;
import project.forwork.api.domain.orderresume.service.OrderResumeService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResumeAutoConfirmService {

    private final OrderResumeService orderResumeService;
    private final OrderRepository orderRepository;

    @Scheduled(cron = "0 0,30 * * * *")
    public void markAsWaiting(){
        updatedOrderStatus(OrderStatus.PAID, OrderStatus.WAIT);
    }

    @Scheduled(cron = "0 0,30 * * * *")
    public void markPartialAsWaiting(){
        updatedOrderStatus(OrderStatus.PARTIAL_CANCEL, OrderStatus.PARTIAL_WAIT);
    }

    @Scheduled(cron = "0 0,30 * * * *")
    public void markAsConfirm(){
        updatedOrderStatus(OrderStatus.WAIT, OrderStatus.CONFIRM);
    }

    @Scheduled(cron = "0 0,30 * * * *")
    public void markPartialAsConfirm(){
        updatedOrderStatus(OrderStatus.PARTIAL_WAIT, OrderStatus.PARTIAL_CONFIRM);
    }


    public void updatedOrderStatus(OrderStatus oldStatus, OrderStatus updatedStatus) {
        int limit = 10;
        while (true) {
            Orders orders = orderRepository.findByStatus(oldStatus, limit);

            // 더 이상 처리할 주문이 없으면 반복 종료
            if (orders.isEmpty()) {
                break;
            }

            orders = orders.updateStatus(updatedStatus);
            Orders updatedOrders = orderRepository.saveAll(orders);
            sendMailByOrderConfirm(updatedOrders, updatedStatus);
        }
    }

    private void sendMailByOrderConfirm(Orders orders, OrderStatus updatedStatus) {
        if (OrderStatus.PARTIAL_CONFIRM.equals(updatedStatus) || OrderStatus.CONFIRM.equals(updatedStatus)) {
            orderResumeService.sendMailForAutoConfirmedOrder(orders); // 주문 확정일 경우 메일 전송
        }
    }
}
