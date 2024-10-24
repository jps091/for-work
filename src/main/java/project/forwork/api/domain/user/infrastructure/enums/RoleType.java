package project.forwork.api.domain.user.infrastructure.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RoleType {

    ADMIN("관리자"),
    USER("일반사용자"),
    ;

    private String description;

    @JsonCreator
    public static RoleType from(String s) {
        for (RoleType status : RoleType.values()) {
            if (status.name().equalsIgnoreCase(s)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid RoleType: " + s);
    }

}
