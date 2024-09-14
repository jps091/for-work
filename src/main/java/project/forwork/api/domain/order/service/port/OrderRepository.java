package project.forwork.api.domain.order.service.port;

import project.forwork.api.domain.order.infrastructure.enums.OrderStatus;
import project.forwork.api.domain.order.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Order save(Order order);
    void saveAll(List<Order> orders);
    Order getByIdWithThrow(long id);
    Optional<Order> findById(long id);
    List<Order> findAllByStatus(OrderStatus status);
}
