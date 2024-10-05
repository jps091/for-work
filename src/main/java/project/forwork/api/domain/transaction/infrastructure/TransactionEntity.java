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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity orderEntity;

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
        txEntity.userEntity = UserEntity.from(tx.getUser());
        txEntity.orderEntity = OrderEntity.from(tx.getOrder());
        txEntity.amount =tx.getAmount();
        txEntity.transactionType = tx.getTransactionType();
        return txEntity;
    }

    public Transaction toModel(){
        return Transaction.builder()
                .id(id)
                .paymentKey(paymentKey)
                .user(userEntity.toModel())
                .order(orderEntity.toModel())
                .amount(amount)
                .transactionType(transactionType)
                .build();
    }
}
