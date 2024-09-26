package project.forwork.api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TokenErrorCode implements ErrorCodeIfs{

    INVALID_TOKEN(401, 2000, "유효 하지 않은 토큰 입니다."),
    EXPIRED_TOKEN(401, 2001, "만료된 토큰 입니다."),
    TOKEN_NOT_FOUND(401, 2002, "인증 헤더 토큰이 존재 하지 않습니다."),
    EXCEPTION_TOKEN(500, 2003, "잠시후 다시 시도 해주세요."),
    ;

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;

}
