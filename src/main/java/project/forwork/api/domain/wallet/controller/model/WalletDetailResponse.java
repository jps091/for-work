package project.forwork.api.domain.wallet.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.forwork.api.domain.wallet.model.Wallet;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WalletDetailResponse {
    private Long walletId;
    private Long userId;
    private BigDecimal balance;
    private LocalDateTime chargedAt;
    private LocalDateTime usedAt;

    public static WalletDetailResponse from(Wallet wallet){
        return WalletDetailResponse.builder()
                .walletId(wallet.getId())
                .userId(wallet.getId())
                .balance(wallet.getBalance())
                .chargedAt(wallet.getChargedAt())
                .usedAt(wallet.getPayedAt())
                .build();
    }
}
