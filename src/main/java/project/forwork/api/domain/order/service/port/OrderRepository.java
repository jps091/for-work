package project.forwork.api.domain.order.service.port;

import project.forwork.api.domain.order.infrastructure.enums.OrderStatus;
import project.forwork.api.domain.order.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);
    void saveAll(List<Order> orders);
    Order getByIdWithThrow(Long orderId);
    Order getOrderWithThrow(Long userId, Long orderId);
    Optional<Order> findById(Long orderId);
    List<Order> findByStatus(OrderStatus status);
}
