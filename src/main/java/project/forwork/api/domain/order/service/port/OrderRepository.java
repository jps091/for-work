package project.forwork.api.domain.order.service.port;

import project.forwork.api.domain.order.infrastructure.enums.OrderStatus;
import project.forwork.api.domain.order.model.Order;

import java.util.List;
import java.util.Optional;

/***
 * index : 1. status 2. requestId
 */
public interface OrderRepository {
    Order save(Order order);
    List<Order> saveAll(List<Order> orders);
    Order getByIdWithThrow(Long orderId);
    Order getOrderWithThrow(Long userId, Long orderId);
    Order getByRequestIdWithThrow(String requestId);
    boolean existsByRequestId(String requestId);
    Optional<Order> findById(Long orderId);
    List<Order> findByStatus(OrderStatus status);
    List<Order> findByUserId(Long userId);
    List<Order> findByStatus(OrderStatus status, int limit);
}
