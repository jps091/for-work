package project.forwork.api.domain.resume.controller.model;

import lombok.*;
import project.forwork.api.domain.resume.infrastructure.enums.FieldType;
import project.forwork.api.domain.resume.infrastructure.enums.LevelType;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.model.Resume;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResumeResponse {

    private Long id;
    private String email;
    private FieldType field;
    private LevelType level;
    private ResumeStatus status;
    private LocalDateTime modifiedAt;

    public static ResumeResponse from(Resume resume){
        return ResumeResponse.builder()
                .id(resume.getId())
                .email(resume.getSeller().getEmail())
                .field(resume.getField())
                .level(resume.getLevel())
                .status(resume.getStatus())
                .modifiedAt(resume.getModifiedAt())
                .build();
    }
}
