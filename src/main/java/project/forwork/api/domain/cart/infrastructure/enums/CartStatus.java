package project.forwork.api.domain.cart.infrastructure.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CartStatus {
    ACTIVE("활성"),
    INACTIVE("비활성"),
    ;

    private String description;

    @JsonCreator
    public static CartStatus from(String s) {
        for (CartStatus status : CartStatus.values()) {
            if (status.name().equalsIgnoreCase(s)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid CartStatus: " + s);
    }
}
