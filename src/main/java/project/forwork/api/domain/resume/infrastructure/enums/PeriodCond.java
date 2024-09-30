package project.forwork.api.domain.resume.infrastructure.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PeriodCond {
    TODAY("오늘"),
    WEEK("1주일"),
    MONTH("1달"),

    ;

    private String description;

    @JsonCreator
    public static ResumeStatus from(String s) {
        for (ResumeStatus status : ResumeStatus.values()) {
            if (status.name().equalsIgnoreCase(s)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid ResumeStatus: " + s);
    }
}
