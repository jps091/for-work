package project.forwork.api.domain.resume.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.forwork.api.common.error.ResumeErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.resume.service.port.ResumeRepository;


import java.util.List;
import java.util.Optional;
@Repository
@RequiredArgsConstructor
public class ResumeRepositoryImpl implements ResumeRepository {

    private final ResumeJpaRepository resumeJpaRepository;


    @Override
    public Resume save(Resume resume) {
        return resumeJpaRepository.save(ResumeEntity.from(resume)).toModel();
    }

    @Override
    public void delete(Resume resume) {
        resumeJpaRepository.delete(ResumeEntity.from(resume));
    }

    @Override
    public Resume getByIdWithThrow(Long id) {
        return findById(id).orElseThrow(() -> new ApiException(ResumeErrorCode.RESUME_NOT_FOUND, id));
    }

    @Override
    public List<Resume> findByIds(List<Long> resumeIds) {
        return resumeJpaRepository.findByIds(resumeIds).stream()
                .map(ResumeEntity::toModel)
                .toList();
    }

    @Override
    public List<Resume> findByIdsWithPessimisticLock(List<Long> resumeIds) {
        return resumeJpaRepository.findByIdsWithPessimisticLock(resumeIds).stream()
                .map(ResumeEntity::toModel)
                .toList();
    }

    @Override
    public Resume getByIdWithPessimisticLock(Long resumeId) {
        return resumeJpaRepository.findByIdWithPessimisticLock(resumeId)
                .map(ResumeEntity::toModel)
                .orElseThrow(() -> new ApiException(ResumeErrorCode.RESUME_NOT_FOUND, resumeId));
    }

    @Override
    public Resume getByIdWithOptimisticLock(Long resumeId) {
        return resumeJpaRepository.findByIdWithOptimisticLock(resumeId)
                .map(ResumeEntity::toModel)
                .orElseThrow(() -> new ApiException(ResumeErrorCode.RESUME_NOT_FOUND, resumeId));
    }

    @Override
    public Optional<Resume> findById(Long id) {
        return resumeJpaRepository.findById(id).map(ResumeEntity::toModel);
    }

    @Override
    public List<Resume> findAllBySeller(Long userId, List<ResumeStatus> statusList) {
        return resumeJpaRepository.findBySellerIdAndStatus(userId, statusList)
                .stream()
                .map(ResumeEntity::toModel)
                .toList();
    }

    @Override
    public List<Resume> saveAll(List<Resume> resumes) {
        List<ResumeEntity> resumeEntities = resumes.stream()
                .map(ResumeEntity::from).toList();

        return resumeJpaRepository.saveAll(resumeEntities).stream()
                .map(ResumeEntity::toModel).toList();
    }
}
