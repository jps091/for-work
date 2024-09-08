package project.forwork.api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResumeErrorCode implements ErrorCodeIfs{

    RESUME_NOT_FOUND(404, 3401, "이력서를 찾을 수 없습니다."),
    ACCESS_NOT_PERMISSION(403, 3402, "이력서 접근 권한이 없습니다."),
    STATUS_NOT_PENDING(404, 3403, "이력서를 수정하려면 대기 상태여야 합니다."),
    STATUS_NOT_ACTIVE(404, 3404, "이력서를 판매 등록 하려면 활성 상태여야 합니다."),
    ;

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;

}
