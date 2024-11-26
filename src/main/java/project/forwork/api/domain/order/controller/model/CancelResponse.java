package project.forwork.api.domain.order.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CancelResponse {
    private BigDecimal cancelAmount;
    private String message;

    public static CancelResponse fromAllCancel(BigDecimal cancelAmount){
        return CancelResponse.builder()
                .cancelAmount(cancelAmount)
                .message("요청 하신 주문 전체 환불을 완료 했습니다.")
                .build();
    }

    public static CancelResponse fromPartCancel(BigDecimal cancelAmount){
        return CancelResponse.builder()
                .cancelAmount(cancelAmount)
                .message("선택 하신 이력서 환불을 완료 했습니다.")
                .build();
    }
}
