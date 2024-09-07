package project.forwork.api.domain.salepost.model;

import lombok.Builder;
import lombok.Getter;
import project.forwork.api.domain.resume.infrastructure.ResumeEntity;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.salepost.infrastructure.enums.SalesStatus;
import project.forwork.api.domain.thumbnailimage.infrastructure.ThumbnailImageEntity;
import project.forwork.api.domain.thumbnailimage.model.ThumbnailImage;

@Getter
public class SalePost {
    private final Long id;
    private final Resume resume;
    private final String title;
    private final ThumbnailImage thumbnail;
    private final SalesStatus salesStatus;
    private final Integer quantity;
    private final Integer viewCount;

    @Builder
    public SalePost(Long id, Resume resume, String title, ThumbnailImage thumbnail, SalesStatus salesStatus, Integer quantity, Integer viewCount) {
        this.id = id;
        this.resume = resume;
        this.title = title;
        this.thumbnail = thumbnail;
        this.salesStatus = salesStatus;
        this.quantity = quantity;
        this.viewCount = viewCount;
    }
}
