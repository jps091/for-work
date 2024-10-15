package project.forwork.api.domain.salespost.infrastructure;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import project.forwork.api.common.infrastructure.BaseTimeEntity;
import project.forwork.api.domain.salespost.infrastructure.enums.SalesStatus;
import project.forwork.api.domain.resume.infrastructure.ResumeEntity;
import project.forwork.api.domain.salespost.model.SalesPost;
import project.forwork.api.domain.thumbnailimage.infrastructure.ThumbnailImageEntity;

@Entity
@Table(name = "sales_posts")
@Getter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SalesPostEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sales_post_id")
    private Long id;

    /*@Version
    private Long version;*/

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private ResumeEntity resumeEntity;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thumbnail_image_id")
    private ThumbnailImageEntity thumbnailImageEntity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SalesStatus salesStatus;

    @Column(name = "sales_quantity", nullable = false)
    private Integer salesQuantity;

    @Column(name = "view_count", nullable = false)
    private Integer viewCount; // TODO 제거

    public static SalesPostEntity from(SalesPost salesPost){
        SalesPostEntity salesPostEntity = new SalesPostEntity();
        salesPostEntity.id = salesPost.getId();
        salesPostEntity.resumeEntity = ResumeEntity.from(salesPost.getResume());
        salesPostEntity.title = salesPost.getTitle();
        salesPostEntity.thumbnailImageEntity = ThumbnailImageEntity.from(salesPost.getThumbnailImage());
        salesPostEntity.salesStatus = salesPost.getSalesStatus();
        salesPostEntity.salesQuantity = salesPost.getSalesQuantity();
        salesPostEntity.viewCount = salesPost.getViewCount();
        //salesPostEntity.version = salesPost.getVersion();
        return salesPostEntity;
    }

    public SalesPost toModel(){
        return SalesPost.builder()
                .id(id)
                .resume(resumeEntity.toModel())
                .title(title)
                .thumbnailImage(thumbnailImageEntity.toModel())
                .salesStatus(salesStatus)
                .salesQuantity(salesQuantity)
                .viewCount(viewCount)
                .registeredAt(getModifiedAt())
                //.version(version)
                .build();
    }
}
