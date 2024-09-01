package project.forwork.api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResumeDecisionErrorCode implements ErrorCodeIfs{

    RESUME_DECISION_NOT_FOUND(404, 4401, "이력서 판매 요청을 찾을 수 없습니다."),
    AUTHOR_MISMATCH(401, 4402, "작성자가 일치 하지 않습니다."),
    ;

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;

}
