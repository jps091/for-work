package project.forwork.api.domain.resumedecision.infrastructure.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum DecisionStatus {
    APPROVE("승인"),
    DENY("거부")
    ;

    private String description;

    @JsonCreator
    public static DecisionStatus from(String s) {
        for (DecisionStatus status : DecisionStatus.values()) {
            if (status.name().equalsIgnoreCase(s)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid DecisionStatus: " + s);
    }
}
