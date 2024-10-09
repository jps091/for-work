package project.forwork.api.domain.order.controller.model;

import lombok.*;
import project.forwork.api.domain.orderresume.controller.model.OrderResumeResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailResponse {

    private Long orderId;
    private LocalDateTime paidAt;
    private String email;
    private List<OrderResumeResponse> orderResumeResponses;
    private BigDecimal totalAmount;
}
