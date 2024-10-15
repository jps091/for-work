package project.forwork.api.domain.salespost.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.forwork.api.common.error.SalesPostErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.resume.infrastructure.ResumeEntity;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.salespost.infrastructure.enums.SalesStatus;
import project.forwork.api.domain.salespost.model.SalesPost;
import project.forwork.api.domain.salespost.service.port.SalesPostRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SalesPostRepositoryImpl implements SalesPostRepository {

    private final SalesPostJpaRepository salesPostJpaRepository;


    @Override
    public SalesPost save(SalesPost salesPost) {
        return salesPostJpaRepository.save(SalesPostEntity.from(salesPost)).toModel();
    }

    @Override
    public SalesPost getByIdWithThrow(Long salesPostId) {
        return findById(salesPostId).orElseThrow(() -> new ApiException(SalesPostErrorCode.SALES_POST_NOT_FOUND, salesPostId));
    }

    @Override
    public SalesPost getByIdWithPessimisticLock(Long salesPostId) {
        return salesPostJpaRepository.findByIdWithPessimisticLock(salesPostId)
                .orElseThrow(() -> new ApiException(SalesPostErrorCode.SALES_POST_NOT_FOUND))
                .toModel();
    }

    @Override
    public SalesPost getByIdWithOptimisticLock(Long salesPostId) {
        return salesPostJpaRepository.findByIdWithOptimisticLock(salesPostId)
                .orElseThrow(() -> new ApiException(SalesPostErrorCode.SALES_POST_NOT_FOUND))
                .toModel();
    }

    @Override
    public Optional<SalesPost> findById(Long salesPostId) {
        return salesPostJpaRepository.findById(salesPostId).map(SalesPostEntity::toModel);
    }

    @Override
    public SalesPost getByResumeWithThrow(Resume resume) {
        return salesPostJpaRepository.findByResumeEntity(ResumeEntity.from(resume))
                .orElseThrow(() -> new ApiException(SalesPostErrorCode.SALES_POST_NOT_FOUND))
                .toModel();
    }

    @Override
    public Optional<SalesPost> findByResume(Resume resume) {
        return salesPostJpaRepository.findByResumeEntity(ResumeEntity.from(resume)).map(SalesPostEntity::toModel);
    }


}
