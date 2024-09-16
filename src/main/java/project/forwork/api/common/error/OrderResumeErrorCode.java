package project.forwork.api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderResumeErrorCode implements ErrorCodeIfs{

    ORDER_NOT_FOUND(404, 9401, "주문 이력서 정보를 찾을 수 없습니다."),
    ;

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;
}
