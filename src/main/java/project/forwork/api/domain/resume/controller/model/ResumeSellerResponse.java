package project.forwork.api.domain.resume.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.forwork.api.common.infrastructure.enums.FieldType;
import project.forwork.api.common.infrastructure.enums.LevelType;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.model.Resume;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResumeSellerResponse {

    private String summary;
    private Integer salesQuantity;
    private FieldType field;
    private LevelType level;
    private ResumeStatus status;
    private LocalDateTime modifiedAt;

    public static ResumeSellerResponse from(Resume resume){
        return ResumeSellerResponse.builder()
                .summary(resume.getDescriptionSummary())
                .salesQuantity(resume.getSalesQuantity())
                .field(resume.getField())
                .level(resume.getLevel())
                .status(resume.getStatus())
                .modifiedAt(resume.getModifiedAt())
                .build();
    }
}
