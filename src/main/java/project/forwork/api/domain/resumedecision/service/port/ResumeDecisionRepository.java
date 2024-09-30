package project.forwork.api.domain.resumedecision.service.port;

import project.forwork.api.domain.resumedecision.model.ResumeDecision;

import java.util.Optional;

public interface ResumeDecisionRepository {

    ResumeDecision save(ResumeDecision resumeDecision);

    void delete(ResumeDecision resumeDecision);

    Optional<ResumeDecision> findById(Long id);

    ResumeDecision getByIdWithThrow(Long id);
}
