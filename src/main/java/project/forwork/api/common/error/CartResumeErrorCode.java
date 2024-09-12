package project.forwork.api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CartResumeErrorCode implements ErrorCodeIfs{

    CART_RESUME_NOT_FOUND(404, 7401, "해당 이력서를 찾을 수 없습니다."),
    RESUME_EXISTS_CART(404, 7402, "장바구니에 해당 이력서가 이미 존재 합니다."),
    ;

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;
}
