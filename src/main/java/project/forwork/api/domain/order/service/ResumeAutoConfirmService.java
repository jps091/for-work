package project.forwork.api.domain.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.domain.order.infrastructure.enums.OrderStatus;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.order.service.port.OrderRepository;
import project.forwork.api.domain.orderresume.service.OrderResumeService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ResumeAutoConfirmService {

    private final OrderRepository orderRepository;
    private final OrderResumeService orderResumeService;

    //@Scheduled(fixedRate = 10000, initialDelay = 10000) // TODO 시간 변경
    public void markAsWaiting(){
        updatedOrderStatus(OrderStatus.PAID, OrderStatus.WAIT);
    }

    //@Scheduled(fixedRate = 15000, initialDelay = 15000)
    public void markPartialAsWaiting(){
        updatedOrderStatus(OrderStatus.PARTIAL_CANCEL, OrderStatus.PARTIAL_WAIT);
    }

    //@Scheduled(fixedRate = 20000, initialDelay = 20000)
    public void markAsConfirm(){
        updatedOrderStatus(OrderStatus.WAIT, OrderStatus.CONFIRM);
    }

    //@Scheduled(fixedRate = 25000, initialDelay = 25000)
    public void markPartialAsConfirm(){
        updatedOrderStatus(OrderStatus.PARTIAL_WAIT, OrderStatus.PARTIAL_CONFIRM);
    }

    public void updatedOrderStatus(OrderStatus oldStatus, OrderStatus updatedStatus) {
        int limit = 10; //TODO 수치 변경 가능
        log.info("updatedOrderStatus {} -> {}", oldStatus, updatedStatus);

        while (true) {
            List<Order> orders = orderRepository.findByStatus(oldStatus, limit);

            // 더 이상 처리할 주문이 없으면 반복 종료
            if (orders.isEmpty()) {
                log.info("No more orders with status: {}", oldStatus);
                break;
            }

            // 상태를 업데이트 후 저장
            orders = updateOrdersByStatus(orders, updatedStatus);

            // 상태가 변경된 후에는 다시 조회하지 않음
            sendMailByOrderConfirm(updatedStatus, orders);
        }
    }

    public List<Order> updateOrdersByStatus(List<Order> orders, OrderStatus status) {
        List<Order> updatedOrders = orders.stream()
                .map(order -> order.updateStatus(status))
                .toList();

        orderRepository.saveAll(updatedOrders);
        return updatedOrders;
    }

    public void sendMailByOrderConfirm(OrderStatus updatedStatus, List<Order> orders) {
        if (updatedStatus.equals(OrderStatus.PARTIAL_CONFIRM) || updatedStatus.equals(OrderStatus.CONFIRM)) {
            orderResumeService.sendMailForAutoConfirmedOrder(orders);
        }
    }
}
