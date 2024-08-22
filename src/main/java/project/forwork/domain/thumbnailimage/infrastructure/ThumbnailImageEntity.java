package project.forwork.domain.thumbnailimage.infrastructure;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import project.forwork.common.infrastructure.BaseTimeEntity;

import project.forwork.common.infrastructure.FieldEntity;


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

    @Column(nullable = false)
    private String url;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id", nullable = false)
    private FieldEntity fieldEntity;
}
