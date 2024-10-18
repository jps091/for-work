package project.forwork.api.domain.transaction.infrastructure;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import project.forwork.api.common.infrastructure.BaseTimeEntity;
import project.forwork.api.domain.order.infrastructure.OrderEntity;
import project.forwork.api.domain.transaction.infrastructure.enums.TransactionType;
import project.forwork.api.domain.transaction.model.Transaction;
import project.forwork.api.domain.user.infrastructure.UserEntity;

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

    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @Column(name = "request_id", nullable = false)
    private String requestId;

    @Column(name = "payment_key", nullable = false)
    private String paymentKey;

    @Column(precision = 7, nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
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
