package project.forwork.api.domain.order.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.forwork.api.domain.order.model.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private Long orderId;
    private String orderTitle;
    private LocalDateTime orderedAt;
    private BigDecimal totalPrice;

    public static OrderResponse from(Order order, String orderTitle){
        return OrderResponse.builder()
                .orderId(order.getId())
                .orderTitle(orderTitle)
                .orderedAt(order.getOrderedAt())
                .totalPrice(order.getTotalPrice())
                .build();
    }
}
