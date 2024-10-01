package project.forwork.api.domain.salespost.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.common.error.SalesPostErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.resume.controller.model.ResumeModifyRequest;
import project.forwork.api.domain.resume.infrastructure.enums.PageStep;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.salespost.controller.model.SalesPostDetailResponse;
import project.forwork.api.domain.salespost.controller.model.SalesPostPage;
import project.forwork.api.domain.salespost.controller.model.SalesPostResponse;
import project.forwork.api.domain.salespost.controller.model.SalesPostSellerResponse;
import project.forwork.api.domain.salespost.infrastructure.enums.*;
import project.forwork.api.domain.salespost.model.SalesPost;
import project.forwork.api.domain.salespost.controller.model.SalesPostFilterCond;
import project.forwork.api.domain.salespost.service.port.SalesPostRepository;
import project.forwork.api.domain.salespost.service.port.SalesPostRepositoryCustom;
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
    private final UserRepository userRepository;

    public void changeSalesStatus(CurrentUser currentUser, Long salesPostId, SalesStatus status){
        Resume resume = validateSellerAndResumeStatus(currentUser, salesPostId);

        SalesPost salesPost = salesPostRepository.getByResumeWithThrow(resume);
        salesPost = salesPost.changeStatus(status);
        salesPostRepository.save(salesPost);
    }

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

    public List<SalesPostSellerResponse> findBySeller(CurrentUser currentUser){
        List<SalesPostSellerResponse> salesResponse = salesPostRepositoryCustom.findBySeller(currentUser.getId());

        if(salesResponse.isEmpty()){
            throw new ApiException(SalesPostErrorCode.SALES_POST_NO_CONTENT);
        }

        return salesResponse;
    }

    // 판매 상태인 판매글 반환시 조회수 1증가
    public SalesPostDetailResponse getSellingPostWithThrow(Long salesPostId){
//      TODO 동시성 이슈 낙관적 잠금,별도의 비동기 처리
        SalesPost salesPost = salesPostRepository.getByIdWithThrow(salesPostId);
        salesPost = salesPost.addViewCount();
        salesPostRepository.save(salesPost);

        return salesPostRepositoryCustom.getDetailSalesPost(salesPostId);
    }

    public SalesPostPage getFilteredAndPagedResults(
            SalesPostSortType sortType, BigDecimal minPrice, BigDecimal maxPrice,
            FieldCond field, LevelCond level,
            PageStep pageStep, Long lastId, int limit
    ){
        SalesPostFilterCond cond = SalesPostFilterCond.from(sortType, minPrice, maxPrice, field, level);

        return switch(pageStep){
            case FIRST -> findFirstPage(cond, limit);
            case LAST -> findLastPage(cond, limit);
            case NEXT -> findNextPage(cond, lastId, limit);
            case PREVIOUS -> findPreviousPage(cond, lastId, limit);
        };
    }

    @Transactional(readOnly = true)
    public SalesPostPage findFirstPage(SalesPostFilterCond cond, int limit){
        List<SalesPostResponse> results = salesPostRepositoryCustom.findFirstPage(cond, limit);
        return createSalesPostPage(results);
    }

    @Transactional(readOnly = true)
    public SalesPostPage findLastPage(SalesPostFilterCond cond, int limit){
        List<SalesPostResponse> results = salesPostRepositoryCustom.findLastPage(cond, limit);
        return createSalesPostPage(results);
    }

    @Transactional(readOnly = true)
    public SalesPostPage findNextPage(SalesPostFilterCond cond, Long lastId, int limit){
        List<SalesPostResponse> results = salesPostRepositoryCustom.findNextPage(cond, lastId, limit);
        return createSalesPostPage(results);
    }

    @Transactional(readOnly = true)
    public SalesPostPage findPreviousPage(SalesPostFilterCond cond, Long lastId, int limit){
        List<SalesPostResponse> results = salesPostRepositoryCustom.findPreviousPage(cond, lastId, limit);
        return createSalesPostPage(results);
    }

    private static SalesPostPage createSalesPostPage(List<SalesPostResponse> results) {

        if(results.isEmpty()){
            throw new ApiException(SalesPostErrorCode.SALES_POST_NO_CONTENT);
        }

        SalesPostResponse lastRecord = results.get(results.size() - 1);

        return SalesPostPage.builder()
                .results(results)
                .lastId(lastRecord.getId())
                .build();
    }
}
