package project.forwork.api.domain.transaction.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.domain.transaction.infrastructure.enums.TransactionType;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
public class Transaction {
    private final Long id;
    private final String userEmail;
    private final String requestId;
    private final String paymentKey;
    private final BigDecimal amount;
    private final TransactionType transactionType;

    public static Transaction create(
            CurrentUser currentUser, String requestId, String paymentKey,
            BigDecimal amount, TransactionType type
    ){
        return Transaction.builder()
                .userEmail(currentUser.getEmail())
                .requestId(requestId)
                .paymentKey(paymentKey)
                .amount(amount)
                .transactionType(type)
                .build();
    }
}
