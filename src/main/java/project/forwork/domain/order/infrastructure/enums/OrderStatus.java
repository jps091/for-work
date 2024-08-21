package project.forwork.domain.order.infrastructure.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import project.forwork.domain.user.infrastructure.enums.RoleType;
@AllArgsConstructor
public enum OrderStatus {
    ORDER("주문완료"),
    CANCEL("주문취소"),
    CONFIRM("구매확정")
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
