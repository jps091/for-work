package project.forwork.api.domain.maillog.infrastructure.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import project.forwork.api.common.infrastructure.enums.FieldType;

@AllArgsConstructor
@Getter
public enum Result {
    SUCCESS("발송 성공"),
    FAIL("발송 실패"),
    ;

    private String description;

    @JsonCreator
    public static Result from(String s) {
        for (Result status : Result.values()) {
            if (status.name().equalsIgnoreCase(s)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid Result: " + s);
    }
}
