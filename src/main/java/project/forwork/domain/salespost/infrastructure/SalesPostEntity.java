package project.forwork.domain.salespost.infrastructure;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import project.forwork.common.infrastructure.BaseTimeEntity;
import project.forwork.domain.resume.infrastructure.ResumeEntity;
import project.forwork.domain.salespost.infrastructure.enums.SalesStatus;
import project.forwork.domain.thumbnailimage.infrastructure.ThumbnailImageEntity;
import project.forwork.domain.user.infrastructure.UserEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "sales_posts")
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SalesPostEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sales_post_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    private UserEntity writerEntity;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private ResumeEntity resumeEntity;

    @Column(nullable = false)
    private String title;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thumbnail_image_id", nullable = false)
    private ThumbnailImageEntity thumbnailEntity;

    @Column(precision = 11, scale = 4, nullable = false)
    private BigDecimal price;

    @Column(length = 255, nullable = false)
    private String introduction;

    @Column(length = 255, name = "detail_image_url", nullable = false)
    private String detailImageUrl;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SalesStatus salesStatus; // [SELLING, STOP]
}
