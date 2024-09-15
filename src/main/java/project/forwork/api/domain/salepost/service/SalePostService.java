package project.forwork.api.domain.salepost.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.salepost.controller.model.SalePostPage;
import project.forwork.api.domain.salepost.controller.model.SalePostResponse;
import project.forwork.api.domain.salepost.infrastructure.enums.SalesStatus;
import project.forwork.api.domain.salepost.infrastructure.SalePostSearchCond;
import project.forwork.api.domain.salepost.infrastructure.enums.SalePostSortType;
import project.forwork.api.domain.salepost.model.SalePost;
import project.forwork.api.domain.salepost.service.port.SalePostRepository;

@Service
@Builder
@Transactional
@RequiredArgsConstructor
public class SalePostService {

    private final SellerValidationService sellerValidationService;
    private final SalePostRepository salePostRepository;

    public SalePost register(CurrentUser currentUser, Long resumeId){
        Resume resume = sellerValidationService.validateSellerAndResumeStatus(currentUser, resumeId);
        SalePost salePost = SalePost.create(resume);
        salePost = salePostRepository.save(salePost);
        return salePost;
    }

    public void startSelling(CurrentUser currentUser, Long resumeId){
        Resume resume = sellerValidationService.validateSellerAndResumeStatus(currentUser, resumeId);
        SalePost salePost = salePostRepository.getByResumeWithThrow(resume);
        salePost = salePost.changeStatus(SalesStatus.SELLING);
        salePostRepository.save(salePost);
    }

    public void cancelSelling(CurrentUser currentUser, Long resumeId){
        Resume resume = sellerValidationService.validateSellerAndResumeStatus(currentUser, resumeId);
        SalePost salePost = salePostRepository.getByResumeWithThrow(resume);
        salePost = salePost.changeStatus(SalesStatus.CANCELED);
        salePostRepository.save(salePost);
    }

    public SalePost getSalePostWithThrow(Long salePostId){
        return salePostRepository.getByIdWithThrow(salePostId);
    }

    // 판매 상태인 판매글 반환시 조회수 1증가
    public SalePost getSellingPostWithThrow(Long salePostId){
        SalePost salePost = salePostRepository.getSellingPostWithThrow(salePostId);

//      TODO 동시성 이슈 낙관적 잠금,별도의 비동기 처리
        salePost = salePost.addViewCount();
        salePost = salePostRepository.save(salePost);
        return salePost;
    }

    public SalePostPage getResumesByCondition(
            int offset,
            int limit,
            SalePostSortType sortType,
            SalePostSearchCond cond
    ){
        PageRequest pageRequest = PageRequest.of(offset, limit);
        Page<SalePostResponse> result = salePostRepository.searchByCondition(cond, pageRequest, sortType);
        return SalePostPage.from(result);
    }
}
