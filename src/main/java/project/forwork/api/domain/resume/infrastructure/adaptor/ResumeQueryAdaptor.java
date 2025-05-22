package project.forwork.api.domain.resume.infrastructure.adaptor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.forwork.api.common.error.ResumeErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.resume.infrastructure.ResumeEntity;
import project.forwork.api.domain.resume.infrastructure.ResumeJpaRepository;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.resume.service.port.ResumeQueryPort;

import java.util.List;
@Repository
@RequiredArgsConstructor
public class ResumeQueryAdaptor implements ResumeQueryPort {
    private final ResumeJpaRepository resumeJpaRepository;

    @Override
    public Resume getByIdWithThrow(Long id) {
        return resumeJpaRepository.findById(id).map(ResumeEntity::toModel)
                .orElseThrow(() -> new ApiException(ResumeErrorCode.RESUME_NOT_FOUND, id));
    }

    @Override
    public List<Resume> findAllBySeller(Long userId, List<ResumeStatus> statusList) {
        return resumeJpaRepository.findBySellerIdAndStatus(userId, statusList)
                .stream()
                .map(ResumeEntity::toModel)
                .toList();
    }

    @Override
    public List<Resume> findByIds(List<Long> resumeIds) {
        return resumeJpaRepository.findByIdsWithPessimisticLock(resumeIds).stream()
                .map(ResumeEntity::toModel)
                .toList();
    }
}
