package project.forwork.api.domain.salepost.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import project.forwork.api.common.error.SalePostErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.resume.infrastructure.ResumeEntity;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.salepost.controller.model.SalePostResponse;
import project.forwork.api.domain.salepost.infrastructure.enums.SalesStatus;
import project.forwork.api.domain.salepost.infrastructure.enums.SalePostSortType;
import project.forwork.api.domain.salepost.model.SalePost;
import project.forwork.api.domain.salepost.service.port.SalePostRepository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SalePostRepositoryImpl implements SalePostRepository {

    private final SalePostJpaRepository salePostJpaRepository;
    private final SalePostQueryDslRepository salePostQueryDslRepository;
    @Override
    public SalePost save(SalePost salePost) {
        return salePostJpaRepository.save(SalePostEntity.from(salePost)).toModel();
    }

    @Override
    public SalePost getByIdWithThrow(Long salePostId) {
        return findById(salePostId).orElseThrow(() -> new ApiException(SalePostErrorCode.SALE_POST_NOT_FOUND, salePostId));
    }

    @Override
    public Optional<SalePost> findById(Long salePostId) {
        return salePostJpaRepository.findById(salePostId).map(SalePostEntity::toModel);
    }

    @Override
    public SalePost getSellingPostWithThrow(Long salePostId) {
        return salePostJpaRepository.findSellingPost(salePostId, ResumeStatus.ACTIVE, SalesStatus.SELLING)
                .orElseThrow(() -> new ApiException(SalePostErrorCode.SALE_POST_NOT_FOUND))
                .toModel();
    }

    @Override
    public SalePost getByResumeWithThrow(Resume resume) {
        return salePostJpaRepository.findByResumeEntity(ResumeEntity.from(resume))
                .orElseThrow(() -> new ApiException(SalePostErrorCode.SALE_POST_NOT_FOUND))
                .toModel();
    }

    @Override
    public Page<SalePostResponse> searchByCondition(SalePostSearchCond cond, PageRequest pageRequest, SalePostSortType sortType) {
        return salePostQueryDslRepository.search(cond, pageRequest, sortType);
    }
}
