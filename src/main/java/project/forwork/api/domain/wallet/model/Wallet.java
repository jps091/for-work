package project.forwork.api.domain.wallet.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.common.error.WalletErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.common.service.port.ClockHolder;
import project.forwork.api.domain.user.model.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static project.forwork.api.domain.wallet.service.WalletService.BALANCE_LIMIT;

@AllArgsConstructor
@Builder
@Getter
public class Wallet {
    private final Long id;
    private final User user;
    private final BigDecimal balance;
    private final LocalDateTime chargedAt;
    private final LocalDateTime payedAt;

    public static Wallet create(CurrentUser currentUser){
        return Wallet.builder()
                .id(currentUser.getId())
                .balance(BigDecimal.ZERO)
                .build();
    }

    public Wallet addBalance(BigDecimal amount, ClockHolder clockHolder){
        BigDecimal addedBalance = balance.add(amount);

        if(BALANCE_LIMIT.compareTo(addedBalance) < 0){
            throw new ApiException(WalletErrorCode.BALANCE_EXCEED);
        }

        return Wallet.builder()
                .id(id)
                .user(user)
                .balance(addedBalance)
                .chargedAt(clockHolder.now())
                .build();
    }

    public Wallet minBalance(BigDecimal amount, ClockHolder clockHolder){
        BigDecimal payedBalance = balance.min(amount);

        if(BigDecimal.ZERO.compareTo(payedBalance) > 0){
            throw new ApiException(WalletErrorCode.BALANCE_NOT_ENOUGH);
        }

        return Wallet.builder()
                .id(id)
                .user(user)
                .balance(payedBalance)
                .payedAt(clockHolder.now())
                .build();
    }
}
