package project.forwork.api.domain.resume.infrastructure.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ResumeStatus {

    PENDING("대기"),
    ACTIVE("활성"),
    REJECTED("거절")
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
