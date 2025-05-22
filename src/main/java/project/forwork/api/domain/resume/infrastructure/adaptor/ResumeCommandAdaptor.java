package project.forwork.api.domain.resume.infrastructure.adaptor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.forwork.api.common.error.ResumeErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.cartresume.service.port.CartResumeRepository;
import project.forwork.api.domain.resume.infrastructure.ResumeEntity;
import project.forwork.api.domain.resume.infrastructure.ResumeJpaRepository;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.resume.service.port.ResumeCommandPort;
import project.forwork.api.domain.resumedecision.infrastructure.ResumeDecisionEntity;
import project.forwork.api.domain.resumedecision.infrastructure.ResumeDecisionJpaRepository;
import project.forwork.api.domain.resumedecision.model.ResumeDecision;
import project.forwork.api.domain.salespost.infrastructure.enums.SalesStatus;
import project.forwork.api.domain.salespost.model.SalesPost;
import project.forwork.api.domain.salespost.service.port.SalesPostRepository;
import project.forwork.api.domain.thumbnailimage.model.ThumbnailImage;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ResumeCommandAdaptor implements ResumeCommandPort {

    private final ResumeJpaRepository resumeJpaRepository;
    private final ResumeDecisionJpaRepository resumeDecisionJpaRepository;
    private final SalesPostRepository salesPostRepository;
    private final CartResumeRepository cartResumeRepository;
    @Override
    public Resume save(Resume resume) {
        return resumeJpaRepository.save(ResumeEntity.from(resume)).toModel();
    }

    @Override
    public Resume approve(Resume resume, Long adminId) {
        Resume updated = resume.updateStatus(ResumeStatus.ACTIVE);
        resumeJpaRepository.save(ResumeEntity.from(updated));

        ResumeDecision resumeDecision = ResumeDecision.approve(adminId, resume.getId());
        resumeDecisionJpaRepository.save(ResumeDecisionEntity.from(resumeDecision)).toModel();
        return updated;
    }

    @Override
    public Resume deny(Resume resume, Long adminId) {
        Resume updated = resume.updateStatus(ResumeStatus.REJECTED);
        resumeJpaRepository.save(ResumeEntity.from(updated));

        ResumeDecision resumeDecision = ResumeDecision.deny(adminId, resume.getId());
        resumeDecisionJpaRepository.save(ResumeDecisionEntity.from(resumeDecision)).toModel();

        return updated;
    }

    @Override
    public void delete(Resume resume, Long userId) {
        Resume deletedResume = resume.delete(userId);
        resumeJpaRepository.save(ResumeEntity.from(deletedResume));
        salesPostRepository.deleteByResumeId(resume.getId());
        cartResumeRepository.deleteAllByResumeId(resume.getId());
    }

    @Override
    public void deleteAll(List<Resume> resumeList, Long userId) {
        List<Long> resumeIds = resumeList.stream()
                .map(resume -> resume.delete(userId))
                .map(resume -> resumeJpaRepository.save(
                        ResumeEntity.from(resume.delete(userId))
                ))
                .map(ResumeEntity::getId)
                .toList();

        resumeIds.forEach(resumeId -> {
            salesPostRepository.deleteByResumeId(resumeId);
            cartResumeRepository.deleteAllByResumeId(resumeId);
        });
    }
}
