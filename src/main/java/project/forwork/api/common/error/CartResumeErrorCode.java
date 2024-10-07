package project.forwork.api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CartResumeErrorCode implements ErrorCodeIfs{

    CART_RESUME_NOT_FOUND(404, 7401, "해당 이력서를 찾을 수 없습니다."),
    RESUME_EXISTS_CART(400, 7402, "장바구니에 해당 이력서가 이미 존재 합니다."),
    NOT_SELECTED(400, 7403, "선택한 이력서가 없습니다. 이력서를 선택 해주세요."),
    RETRY_SELECT(400, 7404, "장바구니에서 이력서를 다시 선택 해주세요."),
    ;

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;
}
