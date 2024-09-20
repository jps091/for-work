package project.forwork.api.domain.resume.controller.model;

import lombok.*;
import project.forwork.api.domain.resume.infrastructure.enums.FieldType;
import project.forwork.api.domain.resume.infrastructure.enums.LevelType;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.model.Resume;

import java.math.BigDecimal;
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResumeDetailResponse {

    private Long id;
    private String email;
    private FieldType field;
    private LevelType level;
    private String resumeUrl;
    private String architectureImageUrl;
    private BigDecimal price;
    private String description;
    private ResumeStatus status;

    public static ResumeDetailResponse from(Resume resume){
        return ResumeDetailResponse.builder()
                .id(resume.getId())
                .email(resume.getSeller().getEmail())
                .field(resume.getField())
                .level(resume.getLevel())
                .resumeUrl(resume.getResumeUrl())
                .architectureImageUrl(resume.getArchitectureImageUrl())
                .price(resume.getPrice())
                .description(resume.getDescription())
                .status(resume.getStatus())
                .build();
    }
}
