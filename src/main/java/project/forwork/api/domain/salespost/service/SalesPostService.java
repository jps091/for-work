package project.forwork.api.domain.salespost.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.common.error.SalesPostErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.common.infrastructure.enums.PageStep;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.resume.service.port.ResumeQueryPort;
import project.forwork.api.domain.salespost.controller.model.*;
import project.forwork.api.domain.salespost.infrastructure.enums.*;
import project.forwork.api.domain.salespost.model.SalesPost;
import project.forwork.api.domain.salespost.service.port.SalesPostRepository;
import project.forwork.api.domain.salespost.service.port.SalesPostRepositoryCustom;
import project.forwork.api.domain.thumbnailimage.model.ThumbnailImage;
import project.forwork.api.domain.thumbnailimage.service.port.ThumbnailImageRepository;

import java.math.BigDecimal;

@Service
@Builder
@RequiredArgsConstructor
public class SalesPostService {

    private final SalesPostRepository salesPostRepository;
    private final SalesPostRepositoryCustom salesPostRepositoryCustom;
    private final ResumeQueryPort resumeQueryPort;
    private final SalesPostViewService salesPostViewService;
    private final ThumbnailImageRepository thumbnailImageRepository;

    @Transactional
    public void registerSalesPost(Resume newResume) {
        salesPostRepository.findByResumeId(newResume.getId()).ifPresentOrElse(
                salesPost -> {
                    // 판매 상태를 SELLING으로 변경 후 저장
                    SalesPost newSalesPost = salesPost.changeStatus(SalesStatus.SELLING);
                    salesPostRepository.save(newSalesPost);
                },
                () -> {
                    // 새로운 SalesPost 생성 후 저장
                    ThumbnailImage thumbnailImage = thumbnailImageRepository.getByFieldWithThrow(newResume.getField());
                    SalesPost newSalesPost = SalesPost.create(newResume.getId(), thumbnailImage.getId());
                    salesPostRepository.save(newSalesPost);
                }
        );
    }

    @Transactional
    public void changeSalesStatus(CurrentUser currentUser, Long resumeId, SalesStatus status){
        SalesPost salesPost = salesPostRepository.getByResumeIdWithThrow(resumeId);
        Resume resume = resumeQueryPort.getByIdWithThrow(resumeId);
        validateSellerAndResumeStatus(currentUser, resume);
        salesPost = salesPost.changeStatus(status);
        salesPostRepository.save(salesPost);
    }

    @Transactional
    public void cancelSalesPostIfExistsByResumeId(Long resumeId){
        salesPostRepository.findByResumeId(resumeId).ifPresent(salesPost -> {
            salesPost = salesPost.changeStatus(SalesStatus.CANCELED);
            salesPostRepository.save(salesPost);
        });
    }

    public SalesPostPage searchFilteredResults(
            SalesPostSortType sortType, BigDecimal minPrice, BigDecimal maxPrice,
            FieldCond field, LevelCond level,
            PageStep pageStep, Long lastId, int limit
    ){
        SalesPostFilterCond cond = SalesPostFilterCond.from(sortType, minPrice, maxPrice, field, level);
        return switch(pageStep){
            case FIRST -> salesPostViewService.findFirstPage(cond, limit);
            case LAST -> salesPostViewService.findLastPage(cond, limit);
            case NEXT -> salesPostViewService.findNextPage(cond, lastId, limit);
            case PREVIOUS -> salesPostViewService.findPreviousPage(cond, lastId, limit);
        };
    }

    private void validateSellerAndResumeStatus(CurrentUser currentUser, Resume resume){
        if(resume.isAuthorMismatch(currentUser.getId())){
            throw new ApiException(SalesPostErrorCode.ACCESS_NOT_PERMISSION, currentUser.getId());
        }

        if(resume.isActiveMismatch()){
            throw new ApiException(SalesPostErrorCode.STATUS_NOT_ACTIVE, resume.getId());
        }
    }
}
