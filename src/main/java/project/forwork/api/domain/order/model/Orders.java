package project.forwork.api.domain.order.model;

import lombok.Getter;
import project.forwork.api.common.error.OrderErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.order.infrastructure.OrderEntity;
import project.forwork.api.domain.order.infrastructure.enums.OrderStatus;

import java.util.ArrayList;
import java.util.List;


public class Orders {

    private final List<Order> orders;

    private Orders(List<Order> orders) {
        this.orders = orders;
    }

    public static Orders of(List<Order> orders){
        return new Orders(orders);
    }

    public Orders updateStatus(OrderStatus status){
        List<Order> updatedOrders = new ArrayList<>(orders);
        updatedOrders = updatedOrders.stream()
                .map(order -> order.updateStatus(status))
                .toList();
        return of(updatedOrders);
    }

    public boolean isEmpty(){
        return orders.isEmpty();
    }

    public void validateNotEmpty() {
        if (orders.isEmpty()) {
            throw new ApiException(OrderErrorCode.ORDER_NO_CONTENT);
        }
    }

    public List<OrderEntity> convertToEntity(){
        return orders.stream().map(OrderEntity::from).toList();
    }
}
