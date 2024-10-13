package project.forwork.api.domain.resume.controller.model;

import lombok.*;
import project.forwork.api.common.infrastructure.enums.FieldType;
import project.forwork.api.common.infrastructure.enums.LevelType;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.model.Resume;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResumeResponse {

    private Long id;
    private String summary;
    private FieldType field;
    private LevelType level;
    private ResumeStatus status;
    private LocalDateTime modifiedAt;

    public static ResumeResponse from(Resume resume){
        return ResumeResponse.builder()
                .id(resume.getId())
                .summary(resume.getDescription().substring(0, 15) + "...")
                .field(resume.getField())
                .level(resume.getLevel())
                .status(resume.getStatus())
                .modifiedAt(resume.getModifiedAt())
                .build();
    }
}
