package project.forwork.api.domain.resume.infrastructure;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import project.forwork.api.common.infrastructure.BaseTimeEntity;
import project.forwork.api.common.infrastructure.enums.FieldType;
import project.forwork.api.common.infrastructure.enums.LevelType;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.user.infrastructure.UserEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "resumes")
@Getter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResumeEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resume_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id") @NotNull
    private UserEntity sellerEntity;

    @Enumerated(EnumType.STRING)
    @Column(name = "field") @NotNull
    private FieldType fieldType;

    @Enumerated(EnumType.STRING)
    @Column(name = "level") @NotNull
    private LevelType levelType;

    @NotNull
    @Column(length = 300, name = "resume_url")
    private String resumeUrl;

    @NotNull
    @Column(length = 300, name = "description_image_url")
    private String descriptionImageUrl;

    @NotNull
    @Column(precision = 6, scale = 0)
    private BigDecimal price;

    @Column(name = "sales_quantity") @NotNull
    private Integer salesQuantity;

    @Column(length = 5000) @NotNull
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status") @NotNull
    private ResumeStatus resumeStatus;

    public static ResumeEntity from(Resume resume){
        ResumeEntity resumeEntity = new ResumeEntity();
        resumeEntity.id = resume.getId();
        resumeEntity.sellerEntity = UserEntity.from(resume.getSeller());
        resumeEntity.fieldType = resume.getField();
        resumeEntity.levelType = resume.getLevel();
        resumeEntity.resumeUrl = resume.getResumeUrl();
        resumeEntity.descriptionImageUrl = resume.getDescriptionImageUrl();
        resumeEntity.price = resume.getPrice();
        resumeEntity.salesQuantity = resume.getSalesQuantity();
        resumeEntity.description = resume.getDescription();
        resumeEntity.resumeStatus = resume.getStatus();
        return resumeEntity;
    }

    public Resume toModel(){
        return Resume.builder()
                .id(id)
                .seller(sellerEntity.toModel())
                .field(fieldType)
                .level(levelType)
                .resumeUrl(resumeUrl)
                .descriptionImageUrl(descriptionImageUrl)
                .price(price)
                .description(description)
                .status(resumeStatus)
                .salesQuantity(salesQuantity)
                .registeredAt(getRegisteredAt())
                .build();
    }
}
