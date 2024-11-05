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
    public void saveAll(List<ThumbnailImage> thumbnailImages) {
        List<ThumbnailImageEntity> thumbnailImageEntities = thumbnailImages.stream()
                .map(ThumbnailImageEntity::from).toList();
        thumbnailImageJpaRepository.saveAll(thumbnailImageEntities);
    }

    @Override
    public ThumbnailImage getByIdWithThrow(Long id) {
        return thumbnailImageJpaRepository.findById(id)
                .orElseThrow(() -> new ApiException(ThumbnailImageErrorCode.NOT_FOUND))
                .toModel();
    }

    @Override
    public ThumbnailImage getByFieldWithThrow(FieldType field) {
        return thumbnailImageJpaRepository.findByFieldType(field)
                .orElseThrow(() -> new ApiException(ThumbnailImageErrorCode.NOT_FOUND))
                .toModel();
    }
}
