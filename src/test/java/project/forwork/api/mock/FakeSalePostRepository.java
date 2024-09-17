package project.forwork.api.mock;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import project.forwork.api.common.error.SalePostErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.salepost.controller.model.SalePostResponse;
import project.forwork.api.domain.salepost.infrastructure.enums.SalesStatus;
import project.forwork.api.domain.salepost.infrastructure.SalePostSearchCond;
import project.forwork.api.domain.salepost.infrastructure.enums.SalePostSortType;
import project.forwork.api.domain.salepost.model.SalePost;
import project.forwork.api.domain.salepost.service.port.SalePostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class FakeSalePostRepository implements SalePostRepository {

    private final AtomicLong id = new AtomicLong(0);
    private final List<SalePost> data = new ArrayList<>();


    @Override
    public SalePost save(SalePost salePost) {
        if(salePost.getId() == null || salePost.getId() == 0){
            SalePost newSalePost = SalePost.builder()
                    .id(id.incrementAndGet())
                    .resume(salePost.getResume())
                    .title(salePost.getTitle())
                    .thumbnailImage(salePost.getThumbnailImage())
                    .salesStatus(salePost.getSalesStatus())
                    .quantity(salePost.getQuantity())
                    .viewCount(salePost.getViewCount())
                    .modifiedAt(salePost.getModifiedAt())
                    .build();
            data.add(newSalePost);
            return newSalePost;
        }else{
            data.removeIf(r -> Objects.equals(r.getId(), salePost.getId()));
            data.add(salePost);
            return salePost;
        }
    }

    @Override
    public SalePost getByIdWithThrow(Long id) {
        return findById(id).orElseThrow(() -> new ApiException(SalePostErrorCode.SALE_POST_NOT_FOUND, id));
    }

    @Override
    public Optional<SalePost> findById(Long id) {
        return data.stream().filter(s -> Objects.equals(s.getId(), id)).findAny();
    }

    public SalePost getSellingPostWithThrow(Long salePostId) {
        // 해당 조건 (resumeStatus == ACTIVE && salesStatus == SELLING)에 맞는 SalePost를 찾음
        return data.stream()
                .filter(s -> Objects.equals(s.getId(), salePostId) &&
                        s.getResume().getStatus() == ResumeStatus.ACTIVE &&
                        s.getSalesStatus() == SalesStatus.SELLING)
                .findAny()
                .orElseThrow(() -> new ApiException(SalePostErrorCode.SALE_POST_NOT_FOUND, salePostId));
    }

    @Override
    public SalePost getByResumeWithThrow(Resume resume) {
        return data.stream()
                .filter(s -> Objects.equals(s.getResume(), resume))
                .findAny()
                .orElseThrow(() -> new ApiException(SalePostErrorCode.SALE_POST_NOT_FOUND));
    }
}