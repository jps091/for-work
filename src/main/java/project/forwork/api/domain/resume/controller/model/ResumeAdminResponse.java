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
public class ResumeAdminResponse {

    private Long id;
    private String summary;
    private FieldType field;
    private LevelType level;
    private ResumeStatus status;
    private LocalDateTime registeredAt;
}
