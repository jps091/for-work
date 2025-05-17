package project.forwork.api.domain.order.service.port;

import project.forwork.api.domain.order.infrastructure.enums.OrderStatus;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.order.model.Orders;
import project.forwork.api.domain.orderresume.model.OrderResume;

import java.util.List;
import java.util.Optional;

/***
 * index : 1. status 2. requestId
 */
public interface OrderCommandPort {
    Order save(Order order, List<OrderResume> orderResumes);
    Orders update(Orders orders);
    Order update(Order order);
}
