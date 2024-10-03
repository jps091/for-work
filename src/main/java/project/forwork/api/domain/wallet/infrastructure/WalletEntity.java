package project.forwork.api.domain.wallet.infrastructure;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import project.forwork.api.common.infrastructure.BaseTimeEntity;
import project.forwork.api.domain.user.infrastructure.UserEntity;
import project.forwork.api.domain.wallet.model.Wallet;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "wallets")
@Getter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WalletEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wallet_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @Column(precision = 7, nullable = false)
    private BigDecimal balance;

    @Column(name = "charged_at")
    private LocalDateTime chargedAt;

    @Column(name = "payed_at")
    private LocalDateTime payedAt;

    public static WalletEntity from(Wallet wallet){
        WalletEntity walletEntity = new WalletEntity();
        walletEntity.id = wallet.getId();
        walletEntity.userEntity = UserEntity.from(wallet.getUser());
        walletEntity.balance = wallet.getBalance();
        walletEntity.chargedAt = wallet.getChargedAt();
        walletEntity.payedAt = wallet.getPayedAt();
        return walletEntity;
    }

    public Wallet toModel(){
        return Wallet.builder()
                .id(id)
                .user(userEntity.toModel())
                .balance(balance)
                .chargedAt(chargedAt)
                .payedAt(payedAt)
                .build();
    }
}
