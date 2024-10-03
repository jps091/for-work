package project.forwork.api.domain.transaction.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import project.forwork.api.common.service.port.ClockHolder;
import project.forwork.api.domain.transaction.controller.model.TransactionCreateRequest;
import project.forwork.api.domain.transaction.infrastructure.enums.TransactionType;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.domain.wallet.model.Wallet;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class Transaction {
    private final Long id;
    private final User user;
    private final Wallet wallet;
    private final String orderTxId;
    private final BigDecimal amount;
    private final TransactionType transactionType;
    private final LocalDateTime chargedAt;
    private final LocalDateTime payedAt;

    public static Transaction createChargeTx(User user, Wallet wallet, TransactionCreateRequest body, ClockHolder clockHolder){
        return Transaction.builder()
                .user(user)
                .wallet(wallet)
                .orderTxId(body.getOrderTxId())
                .amount(body.getAmount())
                .transactionType(TransactionType.CHARGE)
                .chargedAt(clockHolder.now())
                .build();
    }

    public static Transaction createPaymentTx(User user, Wallet wallet, TransactionCreateRequest body, ClockHolder clockHolder){
        return Transaction.builder()
                .user(user)
                .wallet(wallet)
                .orderTxId(body.getOrderTxId())
                .amount(body.getAmount())
                .transactionType(TransactionType.PAYMENT)
                .payedAt(clockHolder.now())
                .build();
    }
}
