package project.forwork.api.domain.resume.service.port;

import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.model.Resume;

import java.util.List;
import java.util.Optional;

public interface ResumeQueryPort {
    Resume getByIdWithThrow(Long id);
    List<Resume> findAllBySeller(Long userId, List<ResumeStatus> statusList);
    List<Resume> findByIds(List<Long> resumeIds);
}
