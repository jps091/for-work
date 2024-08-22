package project.forwork.common.infrastructure.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum FieldType {

    FRONTEND("프론트엔드"),
    BACKEND("백엔드"),
    ANDROID("안드로이드"),
    IOS("IOS"),
    DEVOPS("인프라"),
    AI("인공지능"),
    ETC("기타")
    ;

    private String description;

    @JsonCreator
    public static FieldType from(String s) {
        for (FieldType status : FieldType.values()) {
            if (status.name().equalsIgnoreCase(s)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid Field: " + s);
    }
}
