package project.forwork.api.domain.order.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.forwork.api.domain.order.model.Order;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {

    private Long orderId;
    private LocalDateTime paidAt;

    public static PaymentResponse from(Order order){
        return PaymentResponse.builder()
                .orderId(order.getId())
                .paidAt(order.getPaidAt())
                .build();
    }
}
