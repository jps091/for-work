package project.forwork.api.domain.thumbnailimage.infrastructure;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import project.forwork.api.common.infrastructure.BaseTimeEntity;

import project.forwork.api.common.infrastructure.enums.FieldType;
import project.forwork.api.domain.thumbnailimage.model.ThumbnailImage;


@Entity
@Table(name = "thumbnail_images")
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ThumbnailImageEntity extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "thumbnail_image_id")
    private Long id;

    @Column(length = 255) @NotNull
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(name = "field", unique = true) @NotNull
    private FieldType fieldType;

    public static ThumbnailImageEntity from(ThumbnailImage thumbnailImage){
        ThumbnailImageEntity thumbnailImageEntity = new ThumbnailImageEntity();
        thumbnailImageEntity.id = thumbnailImage.getId();
        thumbnailImageEntity.url = thumbnailImage.getUrl();
        thumbnailImageEntity.fieldType = thumbnailImage.getFieldType();
        return thumbnailImageEntity;
    }

    public ThumbnailImage toModel(){
        return ThumbnailImage.builder()
                .id(id)
                .url(url)
                .fieldType(fieldType)
                .build();
    }
}
