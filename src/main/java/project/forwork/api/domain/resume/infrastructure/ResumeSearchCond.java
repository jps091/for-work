package project.forwork.api.domain.resume.infrastructure;

import lombok.Data;
import project.forwork.api.domain.resume.infrastructure.enums.FieldType;
import project.forwork.api.domain.resume.infrastructure.enums.LevelType;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resumedecision.infrastructure.enums.DecisionStatus;

@Data // TODO 관리자가 검색하는 조건은 1일, 1주 정도로 충분 cond 필요없음
public class ResumeSearchCond {
    private ResumeStatus resumeStatus;
    private FieldType field;
    private LevelType level;
}
