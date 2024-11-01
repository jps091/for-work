package project.forwork.api.domain.user.infrastructure.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserStatus {

    ADMIN("관리자"),
    USER("일반 유저"),
    DELETE("탈퇴 유저")
    ;

    private String description;

    @JsonCreator
    public static UserStatus from(String s) {
        for (UserStatus status : UserStatus.values()) {
            if (status.name().equalsIgnoreCase(s)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid Status: " + s);
    }

}
