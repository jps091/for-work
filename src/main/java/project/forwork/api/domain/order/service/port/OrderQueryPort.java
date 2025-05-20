package project.forwork.api.domain.order.service.port;

import project.forwork.api.domain.order.infrastructure.enums.OrderStatus;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.order.model.Orders;
import project.forwork.api.domain.orderresume.infrastructure.enums.OrderResumeStatus;
import project.forwork.api.domain.orderresume.model.OrderResume;
import project.forwork.api.domain.orderresume.model.OrderResumes;

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
    OrderResumes findByOrderId(Long orderId);
    OrderResume getByIdWithThrow(long orderResumeId);
    OrderResumes findByOrderIdAndStatus(List<Long> orderResumeIds, Long orderId, OrderResumeStatus status);
    OrderResumes findByStatusAndOrder(OrderResumeStatus status, Order order);
    OrderResumes findByStatusAndOrders(OrderResumeStatus status, Orders orders);
}
