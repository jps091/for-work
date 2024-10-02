package project.forwork.api.domain.wallet.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BalanceUpdateRequest {
    private Long userId;
    private BigDecimal amount;
}
