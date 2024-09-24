package project.forwork.api.domain.salespost.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.common.error.SalesPostErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.salespost.controller.model.SalesPostPage;
import project.forwork.api.domain.salespost.controller.model.SalesPostResponse;
import project.forwork.api.domain.salespost.infrastructure.SalesPostQueryDslRepository;
import project.forwork.api.domain.salespost.infrastructure.enums.SalesStatus;
import project.forwork.api.domain.salespost.infrastructure.SalesPostSearchCond;
import project.forwork.api.domain.salespost.infrastructure.enums.SalesPostSortType;
import project.forwork.api.domain.salespost.model.SalesPost;
import project.forwork.api.domain.salespost.service.port.SalesPostRepository;

import java.util.List;

@Service
@Builder
@Transactional
@RequiredArgsConstructor
public class SalesPostService {

    private final SellerValidationService sellerValidationService;
    private final SalesPostRepository salesPostRepository;
    private final SalesPostQueryDslRepository salesPostQueryDslRepository;

    public SalesPost register(CurrentUser currentUser, Long resumeId){
        Resume resume = sellerValidationService.validateSellerAndResumeStatus(currentUser, resumeId);
        SalesPost salesPost = SalesPost.create(resume);
        salesPost = salesPostRepository.save(salesPost);
        return salesPost;
    }

    public void startSelling(CurrentUser currentUser, Long resumeId){
        Resume resume = sellerValidationService.validateSellerAndResumeStatus(currentUser, resumeId);
        SalesPost salesPost = salesPostRepository.getByResumeWithThrow(resume);
        salesPost = salesPost.changeStatus(SalesStatus.SELLING);
        salesPostRepository.save(salesPost);
    }

    public void cancelSelling(CurrentUser currentUser, Long resumeId){
        Resume resume = sellerValidationService.validateSellerAndResumeStatus(currentUser, resumeId);
        SalesPost salesPost = salesPostRepository.getByResumeWithThrow(resume);
        salesPost = salesPost.changeStatus(SalesStatus.CANCELED);
        salesPostRepository.save(salesPost);
    }

    public SalesPost getSalesPostWithThrow(Long salesPostId){
        return salesPostRepository.getByIdWithThrow(salesPostId);
    }

    // 판매 상태인 판매글 반환시 조회수 1증가
    public SalesPost getSellingPostWithThrow(Long salesPostId){
        SalesPost salesPost = salesPostRepository.getSellingPostWithThrow(salesPostId);

//      TODO 동시성 이슈 낙관적 잠금,별도의 비동기 처리
        salesPost = salesPost.addViewCount();
        salesPost = salesPostRepository.save(salesPost);
        return salesPost;
    }

    @Transactional(readOnly = true)
    public SalesPostPage findFirstPage(
            SalesPostSortType sortType, SalesPostSearchCond cond, int limit
    ){
        List<SalesPostResponse> results = salesPostQueryDslRepository.findFirstPage(cond, sortType, limit);
        return createSalesPostPage(results);
    }
    @Transactional(readOnly = true)
    public SalesPostPage findLastPage(
            SalesPostSortType sortType, SalesPostSearchCond cond, int limit
    ){
        List<SalesPostResponse> results = salesPostQueryDslRepository.findLastPage(cond, sortType, limit);
        return createSalesPostPage(results);
    }

    @Transactional(readOnly = true)
    public SalesPostPage findNextPage(
            SalesPostSortType sortType, SalesPostSearchCond cond, int limit, Long lastId
    ){
        List<SalesPostResponse> results = salesPostQueryDslRepository.findNextPage(cond, lastId, sortType, limit);

        if(results.isEmpty()){
            throw new ApiException(SalesPostErrorCode.SALES_POST_NO_CONTENT);
        }

        return createSalesPostPage(results);
    }

    @Transactional(readOnly = true)
    public SalesPostPage findPreviousPage(
            SalesPostSortType sortType, SalesPostSearchCond cond, int limit, Long lastId
    ){
        List<SalesPostResponse> results = salesPostQueryDslRepository.findPreviousPage(cond, lastId, sortType, limit);
        return createSalesPostPage(results);
    }
    private static SalesPostPage createSalesPostPage(List<SalesPostResponse> results) {
        SalesPostResponse lastRecord = results.get(results.size() - 1);

        return SalesPostPage.builder()
                .results(results)
                .lastId(lastRecord.getId())
                .build();
    }
}
