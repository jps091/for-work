package project.forwork.api.domain.transaction.infrastructure;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import project.forwork.api.common.infrastructure.BaseTimeEntity;
import project.forwork.api.domain.transaction.infrastructure.enums.TransactionType;
import project.forwork.api.domain.transaction.model.Transaction;
import project.forwork.api.domain.user.infrastructure.UserEntity;
import project.forwork.api.domain.wallet.infrastructure.WalletEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TransactionEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(precision = 7, nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TransactionType transactionType;

    @Column(name = "charged_at")
    private LocalDateTime chargedAt;

    @Column(name = "payed_at")
    private LocalDateTime payedAt;

    public static TransactionEntity from(Transaction tx){
        TransactionEntity txEntity = new TransactionEntity();
        txEntity.id = tx.getId();
        txEntity.orderId = tx.getOrderId();
        txEntity.userEntity = UserEntity.from(tx.getUser());
        txEntity.amount =tx.getAmount();
        txEntity.transactionType = tx.getTransactionType();
        txEntity.chargedAt = tx.getChargedAt();
        txEntity.payedAt = tx.getPayedAt();
        return txEntity;
    }

    public Transaction toModel(){
        return Transaction.builder()
                .id(id)
                .orderId(orderId)
                .user(userEntity.toModel())
                .amount(amount)
                .transactionType(transactionType)
                .chargedAt(chargedAt)
                .payedAt(payedAt)
                .build();
    }
}
