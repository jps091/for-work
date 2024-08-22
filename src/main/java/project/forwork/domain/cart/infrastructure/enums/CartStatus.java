package project.forwork.domain.cart.infrastructure.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import project.forwork.common.infrastructure.enums.LevelType;

@AllArgsConstructor
public enum CartStatus {
    ACTIVE("활성"),
    CHECK_OUT("구매완료"),
    ABANDONED("비활성"),
    ;

    private String description;

    @JsonCreator
    public static LevelType from(String s) {
        for (LevelType status : LevelType.values()) {
            if (status.name().equalsIgnoreCase(s)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid LevelType: " + s);
    }
}
