package project.forwork.api.domain.transaction.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import project.forwork.api.common.service.port.ClockHolder;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.transaction.controller.model.TransactionCreateRequest;
import project.forwork.api.domain.transaction.infrastructure.enums.TransactionType;
import project.forwork.api.domain.user.model.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class Transaction {
    private final Long id;
    private final User user;
    private final Order order;
    private final String paymentKey;
    private final BigDecimal amount;
    private final TransactionType transactionType;

    public static Transaction registerPgPayment(User user, Order order, TransactionCreateRequest body){
        return Transaction.builder()
                .user(user)
                .order(order)
                .paymentKey(body.getPaymentKey())
                .amount(body.getAmount())
                .transactionType(body.getType())
                .build();
    }
}
