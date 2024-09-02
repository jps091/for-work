package project.forwork.api.domain.resumedecision.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.forwork.api.domain.resume.infrastructure.enums.FieldType;
import project.forwork.api.domain.resume.infrastructure.enums.LevelType;
import project.forwork.api.domain.resumedecision.infrastructure.ResumeDecision;
import project.forwork.api.domain.resumedecision.infrastructure.enums.DecisionStatus;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResumeDecisionResponse {

    private Long id;
    private FieldType field;
    private LevelType level;
    private DecisionStatus decisionStatus;
    private LocalDateTime registeredAt;
    private LocalDateTime decidedAt;

    public static ResumeDecisionResponse from(ResumeDecision resumeDecision){
        return ResumeDecisionResponse.builder()
                .id(resumeDecision.getId())
                .field(resumeDecision.getResume().getField())
                .level(resumeDecision.getResume().getLevel())
                .decisionStatus(resumeDecision.getDecisionStatus())
                .decidedAt(resumeDecision.getDecidedAt())
                .build();
    }
}
