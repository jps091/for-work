package project.forwork.api.domain.salespost.infrastructure.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import project.forwork.api.common.infrastructure.enums.FieldType;

import static project.forwork.api.common.infrastructure.enums.FieldType.*;

@AllArgsConstructor
@Getter
public enum FieldCond {
    FRONTEND("프론트엔드", FieldType.FRONTEND),
    BACKEND("백엔드", FieldType.BACKEND),
    ANDROID("안드로이드", FieldType.ANDROID),
    IOS("IOS", FieldType.IOS),
    DEVOPS("인프라", FieldType.DEVOPS),
    AI("인공지능", FieldType.AI),
    UNSELECTED("조건X", null)
    ;

    private final String description;
    private final FieldType fieldType;

    @JsonCreator
    public static FieldCond from(String s) {
        for (FieldCond status : FieldCond.values()) {
            if (status.name().equalsIgnoreCase(s)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid Field: " + s);
    }
}
