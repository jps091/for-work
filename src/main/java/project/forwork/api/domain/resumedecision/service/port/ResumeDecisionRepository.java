package project.forwork.api.domain.resumedecision.service.port;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.resumedecision.infrastructure.ResumeDecision;

import java.util.Optional;

public interface ResumeDecisionRepository {

    ResumeDecision save(ResumeDecision resumeDecision);

    void delete(ResumeDecision resumeDecision);

    Optional<ResumeDecision> findById(Long id);

    ResumeDecision getByIdWithThrow(Long id);

    Optional<ResumeDecision> findByResume(Resume resume);

    Page<ResumeDecision> findAllByResumeStatus(PageRequest pageRequest, ResumeStatus resumeStatus);
}
