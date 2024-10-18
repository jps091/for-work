package project.forwork.api.mock;

import project.forwork.api.common.error.OrderErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.order.infrastructure.enums.OrderStatus;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.order.service.port.OrderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class FakeOrderRepository implements OrderRepository {

    private final AtomicLong id = new AtomicLong(0);
    private final List<Order> data = new ArrayList<>();

    @Override
    public Order save(Order order) {
        if(order.getId() == null || order.getId() == 0){
            Order newOrder = Order.builder()
                    .id(id.incrementAndGet())
                    .user(order.getUser())
                    .totalAmount(order.getTotalAmount())
                    .status(OrderStatus.ORDERED)
                    .paidAt(order.getPaidAt())
                    .build();
            data.add(newOrder);
            return newOrder;
        }else{
            data.removeIf(o -> Objects.equals(o.getId(), order.getId()));
            data.add(order);
            return order;
        }
    }

    @Override
    public List<Order> saveAll(List<Order> orders) {
        return orders.stream().map(this::save).toList();
    }

    @Override
    public Order getByIdWithThrow(Long orderId) {
        return findById(orderId).orElseThrow(() -> new ApiException(OrderErrorCode.ORDER_NOT_FOUND, orderId));
    }

    @Override
    public Order getOrderWithThrow(Long userId, Long orderId) {
        return data.stream()
                .filter(o -> Objects.equals(o.getId(), orderId) &&
                Objects.equals(o.getUser().getId(), userId))
                .findAny()
                .orElseThrow(() -> new ApiException(OrderErrorCode.ORDER_NOT_FOUND, orderId));
    }

    @Override
    public Optional<Order> findById(Long orderId) {
        return data.stream().filter(o -> Objects.equals(o.getId(), orderId)).findAny();
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        return data.stream().filter(order -> Objects.equals(order.getUser().getId(), userId)).toList();
    }


    @Override
    public List<Order> findByStatus(OrderStatus status, int limit) {
        return data.stream()
                .filter(order -> Objects.equals(order.getStatus(), status))
                .limit(limit)
                .toList();
    }

    @Override
    public Optional<Order> findByRequestId(String requestId) {
        return data.stream()
                .filter(o -> Objects.equals(o.getRequestId(), requestId))
                .findAny();
    }
}
