package project.forwork.api.domain.resume.infrastructure.querydsl;

import lombok.Data;
import project.forwork.api.domain.resume.infrastructure.enums.FieldType;
import project.forwork.api.domain.resume.infrastructure.enums.LevelType;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resumedecision.infrastructure.enums.DecisionStatus;

@Data
public class ResumeSearchCond {
    private ResumeStatus resumeStatus;
    private FieldType field;
    private LevelType level;
}
