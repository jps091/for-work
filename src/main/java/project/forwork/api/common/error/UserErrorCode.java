package project.forwork.api.common.error;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserErrorCode implements ErrorCodeIfs{

    USER_DUPLICATION(404, 1401, "요청 하신 ID는 이미 존재 합니다."),
    USER_NOT_FOUND(404, 1402, "사용자를 찾을 수 없습니다.")
    ;

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;
}
