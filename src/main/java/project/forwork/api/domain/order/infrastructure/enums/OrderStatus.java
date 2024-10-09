package project.forwork.api.domain.order.infrastructure.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OrderStatus {
    ORDERED("주문 생성"),
    PAYMENT_FAILED("결제 실패"),
    PAID("결제 완료"),
    WAIT("발송 대기"),
    PARTIAL_WAIT("발송 부분 대기"),

    CANCEL("주문 취소"),
    PARTIAL_CANCEL("부분 취소"),
    CONFIRM("구매 확정"),
    PARTIAL_CONFIRM("부분 구매 확정"),
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
