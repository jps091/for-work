package project.forwork.api.domain.thumbnailimage.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import project.forwork.api.common.infrastructure.enums.FieldType;

@Getter
@AllArgsConstructor
@Builder
public class ThumbnailImage {

    private final Long id;
    private final String url;
    private final FieldType fieldType;
}
