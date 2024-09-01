package project.forwork.api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResumeErrorCode implements ErrorCodeIfs{

    RESUME_NOT_FOUND(404, 3401, "이력서를 찾을 수 없습니다."),
    AUTHOR_MISMATCH(401, 3402, "작성자가 일치 하지 않습니다."),
    ;

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;

}
