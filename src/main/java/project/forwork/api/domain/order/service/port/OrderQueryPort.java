package project.forwork.api.domain.order.service.port;

import project.forwork.api.domain.order.infrastructure.enums.OrderStatus;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.order.model.Orders;
import project.forwork.api.domain.orderresume.infrastructure.enums.OrderResumeStatus;
import project.forwork.api.domain.orderresume.model.OrderResume;

import java.util.List;
import java.util.Optional;

/***
 * index : 1. status 2. requestId
 */
public interface OrderQueryPort {
    Order getByIdWithThrow(Long orderId);
    Optional<Order> findByRequestId(String requestId);
    Orders findByUserId(Long userId);
    Orders findByStatus(OrderStatus status, int limit);
    List<OrderResume> findByOrderId(Long orderId);
    OrderResume getByIdWithThrow(long orderResumeId);
    List<OrderResume> findByOrderIdAndStatus(List<Long> orderResumeIds, Long orderId, OrderResumeStatus status);
    List<OrderResume> findByStatusAndOrder(OrderResumeStatus status, Order order);
    List<OrderResume> findByStatusAndOrders(OrderResumeStatus status, Orders orders);
}
