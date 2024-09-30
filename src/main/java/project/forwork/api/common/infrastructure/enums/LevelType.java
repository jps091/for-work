package project.forwork.api.common.infrastructure.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LevelType {

    NEW("신입"),
    JUNIOR("주니어"),
    SENIOR("시니어"),
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
