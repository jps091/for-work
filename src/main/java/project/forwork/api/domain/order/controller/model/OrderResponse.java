package project.forwork.api.domain.order.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.forwork.api.domain.order.infrastructure.enums.OrderStatus;
import project.forwork.api.domain.order.model.Order;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private Long orderId;
    private String orderTitle;
    private OrderStatus status;
    private BigDecimal totalAmount;

    public static OrderResponse from(OrderResponse orderResponse, String orderTitle){
        return OrderResponse.builder()
                .orderId(orderResponse.getOrderId())
                .orderTitle(orderTitle)
                .status(orderResponse.getStatus())
                .totalAmount(orderResponse.getTotalAmount())
                .build();
    }

    public static OrderResponse from(Order order){
        return OrderResponse.builder()
                .orderId(order.getId())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .build();
    }
}
