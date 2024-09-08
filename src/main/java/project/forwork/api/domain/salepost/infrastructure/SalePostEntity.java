package project.forwork.api.domain.salepost.infrastructure;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import project.forwork.api.common.infrastructure.BaseTimeEntity;
import project.forwork.api.domain.salepost.infrastructure.enums.SalesStatus;
import project.forwork.api.domain.resume.infrastructure.ResumeEntity;
import project.forwork.api.domain.salepost.model.SalePost;
import project.forwork.api.domain.thumbnailimage.infrastructure.ThumbnailImageEntity;

@Entity
@Table(name = "sale_posts")
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SalePostEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_post_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private ResumeEntity resumeEntity;

    @Column(nullable = false)
    private String title;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thumbnail_image_id")
    private ThumbnailImageEntity thumbnailImageEntity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SalesStatus salesStatus;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "view_count", nullable = false)
    private Integer viewCount;

    public static SalePostEntity from(SalePost salePost){
        SalePostEntity salePostEntity = new SalePostEntity();
        salePostEntity.id = salePost.getId();
        salePostEntity.resumeEntity = ResumeEntity.from(salePost.getResume());
        salePostEntity.title = salePost.getTitle();
        salePostEntity.thumbnailImageEntity = ThumbnailImageEntity.from(salePost.getThumbnailImage());
        salePostEntity.salesStatus = salePost.getSalesStatus();
        salePostEntity.quantity = salePost.getQuantity();
        salePostEntity.viewCount = salePost.getViewCount();
        return salePostEntity;
    }

    public SalePost toModel(){
        return SalePost.builder()
                .id(id)
                .resume(resumeEntity.toModel())
                .title(title)
                .thumbnailImage(thumbnailImageEntity.toModel())
                .salesStatus(salesStatus)
                .quantity(quantity)
                .viewCount(viewCount)
                .build();
    }
}
