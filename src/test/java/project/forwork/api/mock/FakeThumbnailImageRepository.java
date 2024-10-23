package project.forwork.api.mock;

import project.forwork.api.common.error.ThumbnailImageErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.common.infrastructure.enums.FieldType;
import project.forwork.api.domain.thumbnailimage.model.ThumbnailImage;
import project.forwork.api.domain.thumbnailimage.service.port.ThumbnailImageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class FakeThumbnailImageRepository implements ThumbnailImageRepository {

    private final AtomicLong id = new AtomicLong(0);
    private final List<ThumbnailImage> data = new ArrayList<>();


    @Override
    public ThumbnailImage getByFieldWithThrow(FieldType type) {
        return data.stream().filter(t -> t.getFieldType().equals(type)).findFirst()
                .orElseThrow(() -> new ApiException(ThumbnailImageErrorCode.NOT_FOUND));
    }

    @Override
    public void saveAll(List<ThumbnailImage> thumbnailImages) {
        for (ThumbnailImage thumbnailImage : thumbnailImages) {
            save(thumbnailImage);
        }
    }

    public ThumbnailImage save(ThumbnailImage thumbnailImage) {
        if(thumbnailImage.getId() == null || thumbnailImage.getId() == 0){
            ThumbnailImage saved = ThumbnailImage.builder()
                    .id(id.incrementAndGet())
                    .url(thumbnailImage.getUrl())
                    .fieldType(thumbnailImage.getFieldType())
                    .build();
            data.add(saved);
            return saved;
        }else{
            data.removeIf(u -> Objects.equals(u.getId(), thumbnailImage.getId()));
            data.add(thumbnailImage);
            return thumbnailImage;
        }
    }

    @Override
    public ThumbnailImage getByIdWithThrow(Long id) {
        return data.stream().filter(t -> t.getId().equals(id)).findFirst()
                .orElseThrow(() -> new ApiException(ThumbnailImageErrorCode.NOT_FOUND));
    }
}