package project.forwork.api.domain.resume.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.forwork.api.common.infrastructure.enums.FieldType;
import project.forwork.api.common.infrastructure.enums.LevelType;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.model.Resume;

import java.math.BigDecimal;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResumeSellerDetailResponse {

    private FieldType field;
    private LevelType level;
    private String resumeUrl;
    private String descriptionImageUrl;
    private BigDecimal price;
    private Integer salesQuantity;
    private String description;
    private ResumeStatus status;

    public static ResumeSellerDetailResponse from(Resume resume){
        return ResumeSellerDetailResponse.builder()
                .field(resume.getField())
                .level(resume.getLevel())
                .salesQuantity(resume.getSalesQuantity())
                .resumeUrl(resume.getResumeUrl())
                .descriptionImageUrl(resume.getDescriptionImageUrl())
                .price(resume.getPrice())
                .description(resume.getDescription())
                .status(resume.getStatus())
                .build();
    }
}
