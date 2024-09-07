package project.forwork.api.domain.thumbnailimage.model;

import lombok.Builder;
import lombok.Getter;
import project.forwork.api.domain.resume.infrastructure.enums.FieldType;

@Getter
public class ThumbnailImage {

    private final Long id;
    private final String url;
    private final FieldType fieldType;

    @Builder
    public ThumbnailImage(Long id, String url, FieldType fieldType) {
        this.id = id;
        this.url = url;
        this.fieldType = fieldType;
    }
}
