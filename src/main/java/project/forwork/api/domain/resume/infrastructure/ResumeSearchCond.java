package project.forwork.api.domain.resume.infrastructure;

import lombok.Data;
import project.forwork.api.domain.resume.infrastructure.enums.FieldType;
import project.forwork.api.domain.resume.infrastructure.enums.LevelType;
import project.forwork.api.domain.resume.infrastructure.enums.PeriodCond;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;

@Data
public class ResumeSearchCond {
    private ResumeStatus resumeStatus;
    private PeriodCond periodCond;
}
