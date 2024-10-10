package project.forwork.api.domain.thumbnailimage.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.forwork.api.common.error.ThumbnailImageErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.common.infrastructure.enums.FieldType;
import project.forwork.api.domain.thumbnailimage.model.ThumbnailImage;
import project.forwork.api.domain.thumbnailimage.service.port.ThumbnailImageRepository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ThumbnailImageRepositoryImpl implements ThumbnailImageRepository {

    private final ThumbnailImageJpaRepository thumbnailImageJpaRepository;

    @Override
    public void save(List<ThumbnailImage> thumbnailImages) {
        List<ThumbnailImageEntity> thumbnailImageEntities = thumbnailImages.stream()
                .map(ThumbnailImageEntity::from).toList();
        thumbnailImageJpaRepository.saveAll(thumbnailImageEntities);
    }

    @Override
    public ThumbnailImage getByFieldType(FieldType type) {
        return thumbnailImageJpaRepository.findByFieldType(type)
                .orElseThrow(() -> new ApiException(ThumbnailImageErrorCode.NOT_FOUND))
                .toModel();
    }
}
