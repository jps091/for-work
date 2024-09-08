package project.forwork.api.domain.salepost.model;

import lombok.Builder;
import lombok.Getter;
import project.forwork.api.common.error.ResumeErrorCode;
import project.forwork.api.common.error.SalePostErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.resume.infrastructure.ResumeEntity;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.salepost.infrastructure.enums.SalesStatus;
import project.forwork.api.domain.thumbnailimage.infrastructure.ThumbnailImageEntity;
import project.forwork.api.domain.thumbnailimage.model.ThumbnailImage;

@Getter
public class SalePost {
    private final Long id;
    private final Resume resume;
    private final String title;
    private final ThumbnailImage thumbnailImage;
    private final SalesStatus salesStatus;
    private final Integer quantity;
    private final Integer viewCount;

    @Builder
    public SalePost(Long id, Resume resume, String title, ThumbnailImage thumbnailImage, SalesStatus salesStatus, Integer quantity, Integer viewCount) {
        this.id = id;
        this.resume = resume;
        this.title = title;
        this.thumbnailImage = thumbnailImage;
        this.salesStatus = salesStatus;
        this.quantity = quantity;
        this.viewCount = viewCount;
    }

    public static SalePost create(Resume resume){
        return SalePost.builder()
                .resume(resume)
                .title(resume.createSalePostTitle())
                .thumbnailImage(null)
                .salesStatus(SalesStatus.SELLING)
                .quantity(30)
                .viewCount(0)
                .build();
    }

    public SalePost changeStatus(SalesStatus status){
        return SalePost.builder()
                .resume(resume)
                .title(title)
                .thumbnailImage(thumbnailImage)
                .salesStatus(status)
                .quantity(quantity)
                .viewCount(viewCount)
                .build();
    }

    public SalePost addViewCount(){
        if(salesStatus != SalesStatus.SELLING){
            return SalePost.builder()
                    .resume(resume)
                    .title(title)
                    .thumbnailImage(thumbnailImage)
                    .salesStatus(salesStatus)
                    .quantity(quantity)
                    .viewCount(viewCount)
                    .build();
        }

        return SalePost.builder()
                .resume(resume)
                .title(title)
                .thumbnailImage(thumbnailImage)
                .salesStatus(salesStatus)
                .quantity(quantity)
                .viewCount(viewCount + 1)
                .build();
    }
}
