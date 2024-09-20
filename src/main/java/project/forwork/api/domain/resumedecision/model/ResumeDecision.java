package project.forwork.api.domain.resumedecision.model;

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
    private final LocalDateTime registeredAt;

    @Builder
    public ResumeDecision(Long id, User admin, Resume resume, DecisionStatus decisionStatus, LocalDateTime registeredAt) {
        this.id = id;
        this.admin = admin;
        this.resume = resume;
        this.decisionStatus = decisionStatus;
        this.registeredAt = registeredAt;
    }

    public static ResumeDecision approve(User admin, Resume resume){
        return ResumeDecision.builder()
                .admin(admin)
                .resume(resume)
                .decisionStatus(DecisionStatus.APPROVE)
                .build();
    }

    public static ResumeDecision deny(User admin, Resume resume){
        return ResumeDecision.builder()
                .admin(admin)
                .resume(resume)
                .decisionStatus(DecisionStatus.DENY)
                .build();
    }
}
