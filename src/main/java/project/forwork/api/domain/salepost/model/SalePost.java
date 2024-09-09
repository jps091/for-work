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

import java.time.LocalDateTime;

@Getter
public class SalePost {
    private final Long id;
    private final Resume resume;
    private final String title;
    private final ThumbnailImage thumbnailImage;
    private final SalesStatus salesStatus;
    private final Integer quantity;
    private final Integer viewCount;
    private final LocalDateTime registeredAt;

    @Builder
    public SalePost(Long id, Resume resume, String title, ThumbnailImage thumbnailImage, SalesStatus salesStatus, Integer quantity, Integer viewCount, LocalDateTime registeredAt) {
        this.id = id;
        this.resume = resume;
        this.title = title;
        this.thumbnailImage = thumbnailImage;
        this.salesStatus = salesStatus;
        this.quantity = quantity;
        this.viewCount = viewCount;
        this.registeredAt = registeredAt;
    }

    public static SalePost create(Resume resume){
        return SalePost.builder()
                .resume(resume)
                .title(resume.createSalePostTitle())
                //.thumbnailImage() TODO 썸네일
                .salesStatus(SalesStatus.SELLING)
                .quantity(30)
                .viewCount(0)
                .build();
    }

    public SalePost changeStatus(SalesStatus status){
        return SalePost.builder()
                .id(id)
                .resume(resume)
                .title(title)
                .thumbnailImage(thumbnailImage)
                .salesStatus(status)
                .quantity(quantity)
                .viewCount(viewCount)
                .registeredAt(registeredAt)
                .build();
    }

    public SalePost addViewCount(){
        return SalePost.builder()
                .id(id)
                .resume(resume)
                .title(title)
                .thumbnailImage(thumbnailImage)
                .salesStatus(salesStatus)
                .quantity(quantity)
                .viewCount(viewCount + 1)
                .registeredAt(registeredAt)
                .build();
    }
}
