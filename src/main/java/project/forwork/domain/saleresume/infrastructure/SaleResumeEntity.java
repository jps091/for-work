package project.forwork.domain.saleresume.infrastructure;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import project.forwork.common.infrastructure.BaseTimeEntity;
import project.forwork.common.infrastructure.FieldEntity;
import project.forwork.common.infrastructure.LevelEntity;
import project.forwork.domain.saleresume.infrastructure.enums.SaleResumeStatus;
import project.forwork.domain.user.infrastructure.UserEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "sale_resumes")
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SaleResumeEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_resume_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private UserEntity senderEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id", nullable = false)
    private FieldEntity fieldEntity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "level_id", nullable = false)
    private LevelEntity levelEntity;

    @Column(name = "resume_url", nullable = false)
    private String resumeUrl;

    @Column(name = "architecture_image_url", nullable = false)
    private String architectureImageUrl;

    @Column(precision = 5, scale = 0, nullable = false)
    private BigDecimal price;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SaleResumeStatus resumeStatus;
}