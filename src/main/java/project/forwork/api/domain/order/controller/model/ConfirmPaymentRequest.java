package project.forwork.api.domain.order.controller.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Data
public class ConfirmPaymentRequest {
    @NotNull
    private List<Long> cartResumeIds;
    @NotNull
    private String paymentKey;
    @NotNull
    private String requestId;
    @NotNull
    private BigDecimal amount;
}
