package project.forwork.api.domain.order.infrastructure.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OrderStatus {
    ORDERED("주문 생성"),
    PAYMENT_FAILED("결제 실패"),
    CONFIRM("결제 승인"),
    WAIT("발송 대기"),
    PARTIAL_WAIT("부분 대기"),

    CANCEL("주문 취소"),
    PARTIAL_CANCEL("부분 취소"),
    SEND("발송 완료"),
    PARTIAL_SEND("부분 발송 완료"),
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
