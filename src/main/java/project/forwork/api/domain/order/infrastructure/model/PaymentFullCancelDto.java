package project.forwork.api.domain.order.infrastructure.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class PaymentFullCancelDto {
    private String cancelReason;  // 부분 취소 사유
}
