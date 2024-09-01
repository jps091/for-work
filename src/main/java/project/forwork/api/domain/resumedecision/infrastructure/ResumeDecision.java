package project.forwork.api.domain.resumedecision.infrastructure;

import lombok.Builder;
import lombok.Getter;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.resumedecision.infrastructure.enums.DecisionStatus;
import project.forwork.api.domain.user.model.User;

import java.time.LocalDateTime;
@Getter
public class ResumeDecision {
    private final Long id;
    private final User admin;
    private final Resume resume;
    private final DecisionStatus decisionStatus;
    private final LocalDateTime decidedAt;

    @Builder
    public ResumeDecision(Long id, User admin, Resume resume, DecisionStatus decisionStatus, LocalDateTime decidedAt) {
        this.id = id;
        this.admin = admin;
        this.resume = resume;
        this.decisionStatus = decisionStatus;
        this.decidedAt = decidedAt;
    }
}
