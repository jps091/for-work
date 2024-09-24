package project.forwork.api.domain.salespost.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
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
    private final Integer quantity;
    private final Integer viewCount;
    private final LocalDateTime modifiedAt;

    public static SalesPost create(Resume resume){
        return SalesPost.builder()
                .resume(resume)
                .title(resume.createSalesPostTitle())
                //.thumbnailImage() TODO 썸네일
                .salesStatus(SalesStatus.SELLING)
                .quantity(30)
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
                .quantity(quantity)
                .viewCount(viewCount)
                .modifiedAt(modifiedAt)
                .build();
    }

    public SalesPost addViewCount(){
        return SalesPost.builder()
                .id(id)
                .resume(resume)
                .title(title)
                .thumbnailImage(thumbnailImage)
                .salesStatus(salesStatus)
                .quantity(quantity)
                .viewCount(viewCount + 1)
                .modifiedAt(modifiedAt)
                .build();
    }
}
