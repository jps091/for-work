package project.forwork.api.common.error;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserErrorCode implements ErrorCodeIfs{

    USER_NOT_FOUND(404, 1401, "사용자를 찾을 수 없습니다."),
    EMAIL_NOT_FOUND(404, 1402, "존재하지 않는 이메일입니다."),
    USER_DUPLICATION(409, 1403, "요청하신 ID는 이미 존재합니다."),
    PASSWORD_NOT_MATCH(401, 1404, "비밀번호가 일치하지 않습니다."),
    PASSWORD_ISSUE(401, 1405, "로그인 실패가 5번을 초과하여 임시 비밀번호를 가입 이메일로 전송하였습니다."),
    EMAIL_VERIFY_FAIL(401, 1406, "이메일 인증코드가 일치 하지 않습니다.")
    ;

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;
}
