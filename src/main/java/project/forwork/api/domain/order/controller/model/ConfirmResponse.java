package project.forwork.api.domain.order.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.forwork.api.domain.order.infrastructure.enums.OrderStatus;
import project.forwork.api.domain.order.model.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmResponse {

    private Long orderId;
    private BigDecimal totalAmount;
    private int orderCount;

    public static ConfirmResponse from(Order order, List<Long> resumeIds){
        return ConfirmResponse.builder()
                .orderId(order.getId())
                .orderCount(resumeIds.size())
                .totalAmount(order.getTotalAmount())
                .build();
    }
}
