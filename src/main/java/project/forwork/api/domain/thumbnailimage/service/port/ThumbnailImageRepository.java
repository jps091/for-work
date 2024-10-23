package project.forwork.api.domain.thumbnailimage.service.port;

import project.forwork.api.common.infrastructure.enums.FieldType;
import project.forwork.api.domain.thumbnailimage.model.ThumbnailImage;

import java.util.List;


public interface ThumbnailImageRepository {
    ThumbnailImage getByFieldWithThrow(FieldType type);
    ThumbnailImage getByIdWithThrow(Long id);
    void saveAll(List<ThumbnailImage> thumbnailImages);
}
