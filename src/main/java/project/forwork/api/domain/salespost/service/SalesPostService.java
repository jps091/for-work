package project.forwork.api.domain.salespost.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.common.error.SalesPostErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.orderresume.model.OrderResume;
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
import java.net.SocketTimeoutException;
import java.util.List;

@Service
@Builder
@Slf4j
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

    public void addSalesQuantityWithPessimistic(List<OrderResume> orderResumes){

        List<SalesPost> salesPosts = orderResumes.stream()
                .map(OrderResume::getResume)
                .map(resume -> salesPostRepository.getByResumeWithPessimisticLock(resume.getId()))
                .map(SalesPost::increaseSalesQuantity)
                .toList();

        salesPostRepository.saveAll(salesPosts);
    }

    /*** TODO 배포시 주석 삭제 필요
     * Hibernate -> ObjectOptimisticLockingFailureException, StaleObjectStateException 발생
     * Spring은 ObjectOptimisticLockingFailureException 예외로 감싸서 반환
     * 주의 : 긍적적 락은 mysql에는 없는 개념 (그냥 update 하기전에 확인)
     *        처음 요청 순이 아니라 롤백하고 다시 재요청 순으로 결정됨 (선착순 적용X)
     */
    @Retryable(
            value = ObjectOptimisticLockingFailureException.class,
            maxAttempts = 1000,
            backoff =  @Backoff(delay = 100)
    )
    public void addSalesQuantityWithOptimistic(List<OrderResume> orderResumes){

        List<SalesPost> salesPosts = orderResumes.stream()
                .map(OrderResume::getResume)
                .map(resume -> salesPostRepository.getByResumeWithOptimisticLock(resume.getId()))
                .map(SalesPost::increaseSalesQuantity)
                .toList();

        salesPostRepository.saveAll(salesPosts);
    }

    public SalesPostDetailResponse getSellingPostWithPessimistic(Long salesPostId){
        SalesPost salesPost = salesPostRepository.getByIdWithPessimisticLock(salesPostId);
        salesPost = salesPost.addViewCount();
        salesPostRepository.save(salesPost);

        return salesPostRepositoryCustom.getDetailSalesPost(salesPostId);
    }

    @Retryable(
            value = ObjectOptimisticLockingFailureException.class,
            maxAttempts = 1000,
            backoff =  @Backoff(delay = 100)
    )
    public SalesPostDetailResponse getSellingPostWithOptimistic(Long salesPostId){
        SalesPost salesPost = salesPostRepository.getByIdWithOptimisticLock(salesPostId);
        salesPost = salesPost.addViewCount();
        SalesPost newSalesPost = salesPostRepository.save(salesPost);

        return salesPostRepositoryCustom.getDetailSalesPost(newSalesPost.getId());
    }

    @Transactional(readOnly = true)
    public List<SalesPostSellerResponse> findBySeller(CurrentUser currentUser){
        List<SalesPostSellerResponse> salesResponse = salesPostRepositoryCustom.findBySeller(currentUser.getId());

        if(salesResponse.isEmpty()){
            throw new ApiException(SalesPostErrorCode.SALES_POST_NO_CONTENT);
        }

        return salesResponse;
    }

    public SalesPostPage getFilteredAndPagedResults(
            SalesPostSortType sortType, BigDecimal minPrice, BigDecimal maxPrice,
            FieldCond field, LevelCond level,
            PageStep pageStep, Long lastId, int limit
    ){
        SalesPostFilterCond cond = SalesPostFilterCond.from(sortType, minPrice, maxPrice, field, level);
        log.info("getFilteredAndPagedResults={}", cond);
        return switch(pageStep){
            case FIRST -> findFirstPage(cond, limit);
            case LAST -> findLastPage(cond, limit);
            case NEXT -> findNextPage(cond, lastId, limit);
            case PREVIOUS -> findPreviousPage2(cond, lastId, limit);
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
        return createSalesPostPage2(results);
    }

    @Transactional(readOnly = true)
    public SalesPostPage findNextPage(SalesPostFilterCond cond, Long lastId, int limit){
        List<SalesPostResponse> results = salesPostRepositoryCustom.findNextPage(cond, lastId, limit);
        return createSalesPostPage(results);
    }

    @Transactional(readOnly = true)
    public SalesPostPage findPreviousPage(SalesPostFilterCond cond, Long lastId, int limit){
        List<SalesPostResponse> results = salesPostRepositoryCustom.findPreviousPage(cond, lastId, limit);
        return createSalesPostPage2(results);
    }

    @Transactional(readOnly = true)
    public SalesPostPage findPreviousPage2(SalesPostFilterCond cond, Long lastId, int limit){
        List<SalesPostResponse> results = salesPostRepositoryCustom.findPreviousPage(cond, lastId, limit);
        return createSalesPostPage2(results);
    }

    @Transactional(readOnly = true)
    public SalesPostDetailResponse getSellingPost(Long salesPostId){

        SalesPostDetailResponse salesPostDetailResponse = salesPostRepositoryCustom.getDetailSalesPost(salesPostId);
        if(SalesStatus.CANCELED.equals(salesPostDetailResponse.getStatus())){
            throw new ApiException(SalesPostErrorCode.NOT_SELLING);
        }
        return salesPostDetailResponse;
    }

    private SalesPostPage createSalesPostPage(List<SalesPostResponse> results) {

        if(results.isEmpty()){
            throw new ApiException(SalesPostErrorCode.SALES_POST_NO_CONTENT);
        }

        SalesPostResponse lastRecord = results.get(results.size() - 1);

        return SalesPostPage.builder()
                .results(results)
                .lastId(lastRecord.getId())
                .build();
    }

    private SalesPostPage createSalesPostPage2(List<SalesPostResponse> results) {

        if(results.isEmpty()){
            throw new ApiException(SalesPostErrorCode.SALES_POST_NO_CONTENT);
        }

        SalesPostResponse first = results.get(0);

        return SalesPostPage.builder()
                .results(results)
                .lastId(first.getId())
                .build();
    }
}
