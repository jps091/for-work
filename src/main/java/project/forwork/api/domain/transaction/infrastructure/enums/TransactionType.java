package project.forwork.api.domain.transaction.infrastructure.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TransactionType {

    CHARGE("충전"),
    PAYMENT("결제")
    ;

    private final String description;

    @JsonCreator
    public static TransactionType from(String s) {
        for (TransactionType type : TransactionType.values()) {
            if (type.name().equalsIgnoreCase(s)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid Field: " + s);
    }
}
