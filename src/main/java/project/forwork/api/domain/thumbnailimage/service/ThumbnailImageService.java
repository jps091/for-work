package project.forwork.api.domain.thumbnailimage.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.infrastructure.enums.FieldType;
import project.forwork.api.domain.thumbnailimage.model.ThumbnailImage;
import project.forwork.api.domain.thumbnailimage.service.port.ThumbnailImageRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ThumbnailImageService {

    private final ThumbnailImageRepository thumbnailImageRepository;

    @Transactional
    public void registerToDatabase(){
        thumbnailImageRepository.saveAll(getThumbnailImages());
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "getThumbnailUrl", key = "#fieldType", cacheManager = "caffeineCacheManager")
    public String getThumbnailUrl(FieldType fieldType) {
        ThumbnailImage thumbnailImage = thumbnailImageRepository.getByFieldWithThrow(fieldType);
        return thumbnailImage.getUrl();
    }

    private List<ThumbnailImage> getThumbnailImages() {
        ThumbnailImage aiV1 = createThumbnailImage(FieldType.AI, "https://for-work-730335533510.s3.ap-northeast-2.amazonaws.com/AI.png");
        ThumbnailImage androidV1 = createThumbnailImage(FieldType.ANDROID, "https://for-work-730335533510.s3.ap-northeast-2.amazonaws.com/ANDROID.png");
        ThumbnailImage backendV1 = createThumbnailImage(FieldType.BACKEND, "https://for-work-730335533510.s3.ap-northeast-2.amazonaws.com/BE.png");
        ThumbnailImage devopsV1 = createThumbnailImage(FieldType.DEVOPS, "https://for-work-730335533510.s3.ap-northeast-2.amazonaws.com/DEV.png");
        ThumbnailImage frontendV1 = createThumbnailImage(FieldType.FRONTEND, "https://for-work-730335533510.s3.ap-northeast-2.amazonaws.com/FE.png");
        ThumbnailImage iosV1 = createThumbnailImage(FieldType.IOS, "https://for-work-730335533510.s3.ap-northeast-2.amazonaws.com/IOS.png");

        return List.of(aiV1, androidV1, backendV1, devopsV1, frontendV1, iosV1);
    }

    private ThumbnailImage createThumbnailImage(FieldType type, String s3Url){
        return ThumbnailImage.builder()
                .fieldType(type)
                .url(s3Url)
                .build();
    }
}
