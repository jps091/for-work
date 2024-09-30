package project.forwork.api.domain.resumedecision.controller.model;

import lombok.*;
import project.forwork.api.common.infrastructure.enums.FieldType;
import project.forwork.api.common.infrastructure.enums.LevelType;
import project.forwork.api.domain.resumedecision.model.ResumeDecision;
import project.forwork.api.domain.resumedecision.infrastructure.enums.DecisionStatus;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResumeDecisionResponse {

    private Long id;
    private FieldType field;
    private LevelType level;
    private DecisionStatus decisionStatus;
    private LocalDateTime registeredAt;

    public static ResumeDecisionResponse from(ResumeDecision resumeDecision){
        return ResumeDecisionResponse.builder()
                .id(resumeDecision.getId())
                .field(resumeDecision.getResume().getField())
                .level(resumeDecision.getResume().getLevel())
                .decisionStatus(resumeDecision.getDecisionStatus())
                .registeredAt(resumeDecision.getRegisteredAt())
                .build();
    }
}
