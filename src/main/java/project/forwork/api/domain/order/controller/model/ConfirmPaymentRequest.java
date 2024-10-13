package project.forwork.api.domain.order.controller.model;

import lombok.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Data
public class ConfirmPaymentRequest {
    private String paymentKey;
    private String orderId; // 멱등키
    private String amount;
}
