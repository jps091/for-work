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
    private final Long resumeId;
    private final Long thumbnailId;
    private final SalesStatus salesStatus;
    private final LocalDateTime registeredAt;

    public static SalesPost create(Long resumeId, Long thumbnailId){

        return SalesPost.builder()
                .resumeId(resumeId)
                .thumbnailId(thumbnailId)
                .salesStatus(SalesStatus.SELLING)
                .build();
    }

    public SalesPost changeStatus(SalesStatus status){
        return SalesPost.builder()
                .id(id)
                .resumeId(resumeId)
                .thumbnailId(thumbnailId)
                .salesStatus(status)
                .registeredAt(registeredAt)
                .build();
    }
}
