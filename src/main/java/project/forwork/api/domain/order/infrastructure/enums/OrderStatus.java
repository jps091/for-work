package project.forwork.api.domain.order.infrastructure.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OrderStatus {
    ORDER("결제완료"),
    WAIT("발송대기"),
    CANCEL("주문취소"),
    PARTIAL_CANCEL("부분취소"),
    PARTIAL_WAIT("부분대기"),
    CONFIRM("구매확정"),
    PARTIAL_CONFIRM("부분확정"),
    ;

    private String description;

    @JsonCreator
    public static OrderStatus from(String s) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.name().equalsIgnoreCase(s)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid OrderStatus: " + s);
    }
}
