package project.forwork.api.domain.resumedecision.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import project.forwork.api.common.error.ResumeDecisionErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.resume.infrastructure.ResumeEntity;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.resumedecision.service.port.ResumeDecisionRepository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ResumeDecisionRepositoryImpl implements ResumeDecisionRepository {

    private final ResumeDecisionJpaRepository resumeDecisionJpaRepository;

    @Override
    public ResumeDecision save(ResumeDecision resumeDecision) {
        return resumeDecisionJpaRepository.save(ResumeDecisionEntity.from(resumeDecision)).toModel();
    }

    @Override
    public void delete(ResumeDecision resumeDecision) {
        resumeDecisionJpaRepository.delete(ResumeDecisionEntity.from(resumeDecision));
    }

    @Override
    public Optional<ResumeDecision> findById(Long id) {
        return resumeDecisionJpaRepository.findById(id).map(ResumeDecisionEntity::toModel);
    }

    @Override
    public ResumeDecision getByIdWithThrow(Long id) {
        return findById(id).orElseThrow(() -> new ApiException(ResumeDecisionErrorCode.RESUME_DECISION_NOT_FOUND, id));
    }

    @Override
    public Optional<ResumeDecision> findByResume(Resume resume) {
        return resumeDecisionJpaRepository.findByResumeEntity(ResumeEntity.from(resume)).map(ResumeDecisionEntity::toModel);
    }

    @Override
    public Page<ResumeDecision> findAllByResumeStatus(PageRequest pageRequest, ResumeStatus resumeStatus) {
        return resumeDecisionJpaRepository.findAllByResumeStatus(pageRequest, resumeStatus)
                .map(ResumeDecisionEntity::toModel);
    }
}
