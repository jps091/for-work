package project.forwork.api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CartErrorCode implements ErrorCodeIfs{

    CART_NOT_FOUND(404, 6401, "장바구니를 찾을 수 없습니다."),
    ACCESS_NOT_PERMISSION(403, 6402, "장바구니 접근 권한이 없습니다."),
    ;

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;
}
