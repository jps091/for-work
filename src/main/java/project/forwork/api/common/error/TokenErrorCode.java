package project.forwork.api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TokenErrorCode implements ErrorCodeIfs{

    INVALID_TOKEN(400, 2000, "유효 하지 않은 토큰."),
    EXPIRED_TOKEN(400, 2001, "만료된 토큰"),
    EXCEPTION_TOKEN(400, 2002, "토큰 알수 없는 에러"),
    TOKEN_NOT_FOUND(403, 2003, "인증 헤더 토큰 없음"),
    ;

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;

}
