package project.forwork.api.domain.salespost.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.forwork.api.common.error.SalesPostErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.salespost.model.SalesPost;
import project.forwork.api.domain.salespost.service.port.SalesPostRepository;

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
    public SalesPost getByResumeIdWithThrow(Long resumeId) {
        return salesPostJpaRepository.findByResumeId(resumeId)
                .orElseThrow(() -> new ApiException(SalesPostErrorCode.SALES_POST_NOT_FOUND))
                .toModel();
    }

    @Override
    public Optional<SalesPost> findByResumeId(Long resumeId) {
        return salesPostJpaRepository.findByResumeId(resumeId).map(SalesPostEntity::toModel);
    }

    @Override
    public void deleteByResumeId(Long resumeId) {
        salesPostJpaRepository.deleteByResumeId(resumeId);
    }
}
