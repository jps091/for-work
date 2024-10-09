package project.forwork.api.domain.orderresume.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.forwork.api.domain.orderresume.infrastructure.enums.OrderResumeStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResumeResponse {

    private Long orderResumeId;
    private String title;
    private OrderResumeStatus status;
    private BigDecimal price;
    private LocalDateTime sentAt;
    private LocalDateTime canceledAt;
}
