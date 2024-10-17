package project.forwork.api.domain.order.controller.model;

import lombok.*;



@Builder
@AllArgsConstructor
@Getter
public class ConfirmPaymentDto {
    private String paymentKey;
    private String orderId; // 멱등키
    private String amount;

    public static ConfirmPaymentDto from(ConfirmPaymentRequest body){
        return ConfirmPaymentDto.builder()
                .paymentKey(body.getPaymentKey())
                .orderId(body.getRequestId())
                .amount(body.getAmount().toString())
                .build();
    }
}
