package project.forwork.api.domain.salespost.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.common.error.SalesPostErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.resume.infrastructure.enums.FieldType;
import project.forwork.api.domain.resume.infrastructure.enums.LevelType;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.salespost.controller.model.SalesPostDetailResponse;
import project.forwork.api.domain.salespost.controller.model.SalesPostPage;
import project.forwork.api.domain.salespost.controller.model.SalesPostResponse;
import project.forwork.api.domain.salespost.controller.model.SalesPostSellerResponse;
import project.forwork.api.domain.salespost.infrastructure.enums.SalesStatus;
import project.forwork.api.domain.salespost.infrastructure.enums.SalesPostSortType;
import project.forwork.api.domain.salespost.model.SalesPost;
import project.forwork.api.domain.salespost.service.port.SalesPostRepository;
import project.forwork.api.domain.salespost.service.port.SalesPostRepositoryCustom;

import java.math.BigDecimal;
import java.util.List;

@Service
@Builder
@Transactional
@RequiredArgsConstructor
public class SalesPostService {

    private final SellerValidationService sellerValidationService;
    private final SalesPostRepository salesPostRepository;
    private final SalesPostRepositoryCustom salesPostRepositoryCustom;

    public void register(CurrentUser currentUser, Long resumeId){
        Resume resume = sellerValidationService.validateSellerAndResumeStatus(currentUser, resumeId);
        SalesPost salesPost = SalesPost.create(resume);
        salesPostRepository.save(salesPost);
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

        return salesPostRepositoryCustom.getSellingPostWithThrow(salesPostId);
    }

    @Transactional(readOnly = true)
    public SalesPostPage findFirstPage(
            SalesPostSortType sortType, BigDecimal minPrice, BigDecimal maxPrice,
            FieldType field, LevelType level, int limit
    ){
        List<SalesPostResponse> results = salesPostRepositoryCustom
                .findFirstPage(sortType, minPrice, maxPrice, field, level, limit);
        return createSalesPostPage(results);
    }
    @Transactional(readOnly = true)
    public SalesPostPage findLastPage(
            SalesPostSortType sortType, BigDecimal minPrice, BigDecimal maxPrice,
            FieldType field, LevelType level, int limit
    ){
        List<SalesPostResponse> results = salesPostRepositoryCustom
                .findLastPage(sortType, minPrice, maxPrice, field, level, limit);
        return createSalesPostPage(results);
    }

    @Transactional(readOnly = true)
    public SalesPostPage findNextPage(
            SalesPostSortType sortType, BigDecimal minPrice, BigDecimal maxPrice,
            FieldType field, LevelType level, Long lastId, int limit
    ){
        List<SalesPostResponse> results = salesPostRepositoryCustom
                .findNextPage(sortType, minPrice, maxPrice, field, level, lastId, limit);

        if(results.isEmpty()){
            throw new ApiException(SalesPostErrorCode.SALES_POST_NO_CONTENT);
        }

        return createSalesPostPage(results);
    }

    @Transactional(readOnly = true)
    public SalesPostPage findPreviousPage(
            SalesPostSortType sortType, BigDecimal minPrice, BigDecimal maxPrice,
            FieldType field, LevelType level, Long lastId, int limit
    ){
        List<SalesPostResponse> results = salesPostRepositoryCustom
                .findPreviousPage(sortType, minPrice, maxPrice, field, level, lastId, limit);
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
