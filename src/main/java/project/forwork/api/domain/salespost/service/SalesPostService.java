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
import project.forwork.api.domain.salespost.controller.model.*;
import project.forwork.api.domain.salespost.infrastructure.enums.*;
import project.forwork.api.domain.salespost.model.SalesPost;
import project.forwork.api.domain.salespost.service.port.SalesPostRepository;
import project.forwork.api.domain.salespost.service.port.SalesPostRepositoryCustom;
import project.forwork.api.domain.thumbnailimage.service.ThumbnailImageService;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.domain.user.service.port.UserRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@Builder
@Transactional
@RequiredArgsConstructor
public class SalesPostService {

    private final SalesPostRepository salesPostRepository;
    private final SalesPostRepositoryCustom salesPostRepositoryCustom;
    private final SalesPostPageService salesPostPageService;
    private final UserRepository userRepository;
    private final ThumbnailImageService thumbnailImageService;

    @Transactional
    public void changeSalesStatus(CurrentUser currentUser, Long salesPostId, SalesStatus status){
        Resume resume = validateSellerAndResumeStatus(currentUser, salesPostId);

        SalesPost salesPost = salesPostRepository.getByResumeWithThrow(resume);
        salesPost = salesPost.changeStatus(status);
        salesPostRepository.save(salesPost);
    }

    @Transactional
    public Resume validateSellerAndResumeStatus(CurrentUser currentUser, Long salesPostId){
        User user = userRepository.getByIdWithThrow(currentUser.getId());
        SalesPost salesPost = salesPostRepository.getByIdWithThrow(salesPostId);
        Resume resume = salesPost.getResume();

        if(resume.isAuthorMismatch(user.getId())){
            throw new ApiException(SalesPostErrorCode.ACCESS_NOT_PERMISSION, user.getId());
        }

        if(resume.isActiveMismatch()){
            throw new ApiException(SalesPostErrorCode.STATUS_NOT_ACTIVE, resume.getId());
        }

        return resume;
    }

    @Transactional(readOnly = true)
    public List<SalesPostSellerResponse> findBySeller(CurrentUser currentUser){
        List<SalesPostSellerResponse> salesResponse = salesPostRepositoryCustom.findBySeller(currentUser.getId());

        if(salesResponse.isEmpty()){
            throw new ApiException(SalesPostErrorCode.SALES_POST_NO_CONTENT);
        }

        return salesResponse;
    }

    @Transactional(readOnly = true)
    public SalesPostDetailResponse getSellingPost(Long resumeId){

        SalesPostDetailResponse salesPostDetailResponse = salesPostRepositoryCustom.getDetailSalesPost(resumeId);
        if(SalesStatus.CANCELED.equals(salesPostDetailResponse.getStatus())){
            throw new ApiException(SalesPostErrorCode.NOT_SELLING);
        }
        return salesPostDetailResponse;
    }

    public SalesPostPage searchFilteredResults(
            SalesPostSortType sortType, BigDecimal minPrice, BigDecimal maxPrice,
            FieldCond field, LevelCond level,
            PageStep pageStep, Long lastId, int limit
    ){
        SalesPostFilterCond cond = SalesPostFilterCond.from(sortType, minPrice, maxPrice, field, level);
        return switch(pageStep){
            case FIRST -> salesPostPageService.findFirstPage(cond, limit);
            case LAST -> salesPostPageService.findLastPage(cond, limit);
            case NEXT -> salesPostPageService.findNextPage(cond, lastId, limit);
            case PREVIOUS -> salesPostPageService.findPreviousPage(cond, lastId, limit);
        };
    }
}
