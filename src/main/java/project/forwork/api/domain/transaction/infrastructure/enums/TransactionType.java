package project.forwork.api.domain.transaction.infrastructure.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TransactionType {

    PAYMENT("결제"),
    REFUND("환불"),
    ;

    private final String description;

    @JsonCreator
    public static TransactionType from(String s) {
        for (TransactionType type : TransactionType.values()) {
            if (type.name().equalsIgnoreCase(s)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid TransactionType: " + s);
    }
}
