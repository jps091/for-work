package project.forwork.api.domain.wallet.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.forwork.api.domain.wallet.model.Wallet;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WalletResponse {
    private Long walletId;
    private Long userId;
    private BigDecimal balance;

    public static WalletResponse from(Wallet wallet){
        return WalletResponse.builder()
                .walletId(wallet.getId())
                .userId(wallet.getId())
                .balance(wallet.getBalance())
                .build();
    }
}
