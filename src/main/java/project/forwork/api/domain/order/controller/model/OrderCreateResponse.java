package project.forwork.api.domain.order.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.forwork.api.domain.order.model.Order;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateResponse {

    private Long orderId;
    private String requestId;
    private String email;
    private BigDecimal totalPrice;

    public static OrderCreateResponse from(Order order){
        return OrderCreateResponse.builder()
                .orderId(order.getId())
                .requestId(order.getRequestId())
                .email(order.getBuyerEmail())
                .totalPrice(order.getTotalAmount())
                .build();
    }
}
