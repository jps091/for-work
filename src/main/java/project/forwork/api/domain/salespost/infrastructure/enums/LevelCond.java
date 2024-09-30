package project.forwork.api.domain.salespost.infrastructure.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import project.forwork.api.common.infrastructure.enums.LevelType;

@AllArgsConstructor
@Getter
public enum LevelCond {

    NEW("신입", LevelType.NEW),
    JUNIOR("주니어", LevelType.JUNIOR),
    SENIOR("시니어", LevelType.SENIOR),
    UNSELECTED("조건X", null)
    ;

    private String description;
    private LevelType levelType;

    @JsonCreator
    public static LevelCond from(String s) {
        for (LevelCond status : LevelCond.values()) {
            if (status.name().equalsIgnoreCase(s)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid LevelType: " + s);
    }
}
