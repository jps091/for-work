package project.forwork.api.domain.maillog.infrastructure.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EmailType {
    BUYER("구매자"),
    SELLER("판매자"),
    VERIFY("인증"),
    NOTICE("공지"),
    ;

    private String description;

    @JsonCreator
    public static EmailType from(String s) {
        for (EmailType status : EmailType.values()) {
            if (status.name().equalsIgnoreCase(s)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid Result: " + s);
    }
}
