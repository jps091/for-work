package project.forwork.api.domain.transaction.infrastructure;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import project.forwork.api.common.infrastructure.BaseTimeEntity;
import project.forwork.api.domain.transaction.infrastructure.enums.TransactionType;
import project.forwork.api.domain.transaction.model.Transaction;

import java.math.BigDecimal;

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

    @NotNull
    @Column(length = 50, name = "user_email")
    private String userEmail;

    @NotNull
    @Column(length = 25, name = "request_id")
    private String requestId;

    @NotNull
    @Column(length = 25, name = "payment_key")
    private String paymentKey;

    @Column(precision = 7) @NotNull
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "type") @NotNull
    private TransactionType transactionType;

    public static TransactionEntity from(Transaction tx){
        TransactionEntity txEntity = new TransactionEntity();
        txEntity.id = tx.getId();
        txEntity.paymentKey = tx.getPaymentKey();
        txEntity.userEmail = tx.getUserEmail();
        txEntity.requestId = tx.getRequestId();
        txEntity.amount =tx.getAmount();
        txEntity.transactionType = tx.getTransactionType();
        return txEntity;
    }

    public Transaction toModel(){
        return Transaction.builder()
                .id(id)
                .paymentKey(paymentKey)
                .userEmail(userEmail)
                .requestId(requestId)
                .amount(amount)
                .transactionType(transactionType)
                .build();
    }
}
