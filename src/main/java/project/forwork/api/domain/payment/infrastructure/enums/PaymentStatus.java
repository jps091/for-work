package project.forwork.api.domain.payment.infrastructure.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PaymentStatus {
    COMPLETED("결제완료"),
    CANCEL("결제취소"),
    WAITING("결제대기")
    ;

    private String description;

    @JsonCreator
    public static PaymentStatus from(String s) {
        for (PaymentStatus status : PaymentStatus.values()) {
            if (status.name().equalsIgnoreCase(s)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid PaymentStatus: " + s);
    }
}
