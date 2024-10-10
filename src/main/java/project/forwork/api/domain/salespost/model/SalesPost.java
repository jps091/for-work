package project.forwork.api.domain.salespost.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import project.forwork.api.common.error.SalesPostErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.common.infrastructure.enums.FieldType;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.salespost.infrastructure.enums.SalesStatus;
import project.forwork.api.domain.thumbnailimage.model.ThumbnailImage;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class SalesPost {
    private final Long id;
    private final Resume resume;
    private final String title;
    private final ThumbnailImage thumbnailImage;
    private final SalesStatus salesStatus;
    private final Integer salesQuantity;
    private final Integer viewCount;
    private final LocalDateTime registeredAt;

    public static SalesPost create(Resume resume, ThumbnailImage thumbnailImage){

        return SalesPost.builder()
                .resume(resume)
                .title(resume.createSalesPostTitle())
                .thumbnailImage(thumbnailImage)
                .salesStatus(SalesStatus.SELLING)
                .salesQuantity(0)
                .viewCount(0)
                .build();
    }

    public SalesPost changeStatus(SalesStatus status){
        return SalesPost.builder()
                .id(id)
                .resume(resume)
                .title(title)
                .thumbnailImage(thumbnailImage)
                .salesStatus(status)
                .salesQuantity(salesQuantity)
                .viewCount(viewCount)
                .registeredAt(registeredAt)
                .build();
    }

    public SalesPost addViewCount(){
        return SalesPost.builder()
                .id(id)
                .resume(resume)
                .title(title)
                .thumbnailImage(thumbnailImage)
                .salesStatus(salesStatus)
                .salesQuantity(salesQuantity)
                .viewCount(viewCount + 1)
                .registeredAt(registeredAt)
                .build();
    }

    public Resume getResumeIfSalesPostSelling(){
        if(SalesStatus.CANCELED.equals(salesStatus)){
            throw new ApiException(SalesPostErrorCode.NOT_SELLING);
        }
        return resume;
    }
}
