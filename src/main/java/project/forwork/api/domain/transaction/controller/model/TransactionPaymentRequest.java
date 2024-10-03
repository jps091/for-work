package project.forwork.api.domain.transaction.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TransactionPaymentRequest {
    private String payResumeId;
    private BigDecimal amount;
}
