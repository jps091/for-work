package project.forwork.api.domain.resumedecision.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.resumedecision.infrastructure.ResumeDecision;
import project.forwork.api.domain.resumedecision.infrastructure.enums.DecisionStatus;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResumeDecisionResponse {

    private Long id;
    private Resume resume;
    private DecisionStatus decisionStatus;
    private LocalDateTime decidedAt;

    public static ResumeDecisionResponse from(ResumeDecision resumeDecision){
        return ResumeDecisionResponse.builder()
                .id(resumeDecision.getId())
                .resume(resumeDecision.getResume())
                .decisionStatus(resumeDecision.getDecisionStatus())
                .decidedAt(resumeDecision.getDecidedAt())
                .build();
    }
}
