package project.forwork.api.domain.saleresumedecision.infrastructure.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum DecisionStatus {
    APPROVED("승인"),
    REJECTED("거부"),
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
