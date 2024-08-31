package project.forwork.api.domain.resume.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.forwork.api.domain.resume.infrastructure.enums.FieldType;
import project.forwork.api.domain.resume.infrastructure.enums.LevelType;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.user.model.User;

import java.math.BigDecimal;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResumeResponse {

    private Long id;
    private User seller;
    private FieldType field;
    private LevelType level;
    private String resumeUrl;
    private String architectureImageUrl;
    private BigDecimal price;
    private String description;
    private ResumeStatus status;

    public static ResumeResponse from(Resume resume){
        return ResumeResponse.builder()
                .id(resume.getId())
                .seller(resume.getSeller())
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
