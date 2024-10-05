package project.forwork.api.domain.transaction.controller.model;

import lombok.*;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.transaction.infrastructure.enums.TransactionType;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Data
public class TransactionCreateRequest {
    private Long userId;
    private Long orderId;
    private String paymentKey;
    private BigDecimal amount;
    private TransactionType type;
}
