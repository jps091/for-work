package project.forwork.api.domain.resumedecision.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import project.forwork.api.common.error.ResumeErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.resumedecision.infrastructure.enums.DecisionStatus;
import project.forwork.api.domain.user.model.User;

import java.time.LocalDateTime;
@Getter
@Builder
@AllArgsConstructor
public class ResumeDecision {
    private final Long id;
    private final Long adminId;
    private final Long resumeId;
    private final DecisionStatus decisionStatus;
    private final LocalDateTime registeredAt;

    public static ResumeDecision approve(Long adminId, Long resumeId){
        return ResumeDecision.builder()
                .adminId(adminId)
                .resumeId(resumeId)
                .decisionStatus(DecisionStatus.APPROVE)
                .build();
    }

    public static ResumeDecision deny(Long adminId, Long resumeId){
        return ResumeDecision.builder()
                .adminId(adminId)
                .resumeId(resumeId)
                .decisionStatus(DecisionStatus.DENY)
                .build();
    }
}
