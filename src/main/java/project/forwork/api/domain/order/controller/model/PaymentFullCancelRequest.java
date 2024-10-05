package project.forwork.api.domain.order.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Builder
@Getter
public class PaymentFullCancelRequest {
    private String cancelReason;  // 부분 취소 사유
}
