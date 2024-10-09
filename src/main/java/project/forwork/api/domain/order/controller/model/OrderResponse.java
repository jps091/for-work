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

    public static OrderResponse from(Order order, String orderTitle){
        return OrderResponse.builder()
                .orderId(order.getId())
                .orderTitle(orderTitle)
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .build();
    }
}
