package project.forwork.api.domain.orderresume.infrastructure.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OrderResumeStatus {
    PAID("결제완료"),
    CANCEL("주문취소"),
    CONFIRM("구매확정"),
    SENT("발송완료")
    ;

    private String description;

    @JsonCreator
    public static OrderResumeStatus from(String s) {
        for (OrderResumeStatus status : OrderResumeStatus.values()) {
            if (status.name().equalsIgnoreCase(s)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid OrderResumeStatus: " + s);
    }
}
