package project.forwork.domain.user.infrastructure.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum InterestField {

    FRONTEND("프론트엔드"),
    BACKEND("백엔드"),
    ANDROID("안드로이드"),
    IOS("IOS"),
    DEVOPS("인프라"),
    AI("인공지능")
    ;

    private String description;

    @JsonCreator
    public static InterestField from(String s) {
        for (InterestField status : InterestField.values()) {
            if (status.name().equalsIgnoreCase(s)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid InterestField: " + s);
    }
}
