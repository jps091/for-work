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
public class ResumeAdminResponse {

    private Long id;
    private String email;
    private FieldType field;
    private LevelType level;
    private ResumeStatus status;

    public static ResumeAdminResponse from(Resume resume){
        return ResumeAdminResponse.builder()
                .id(resume.getId())
                .email(resume.getSeller().getEmail())
                .field(resume.getField())
                .level(resume.getLevel())
                .status(resume.getStatus())
                .build();
    }
}
