package project.forwork.api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderErrorCode implements ErrorCodeIfs{

    ORDER_NOT_FOUND(404, 8401, "주문 정보를 찾을 수 없습니다."),
    ORDER_NOT_PERMISSION(403, 8402, "주문에 대한 권한이 없습니다."),
    ;

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;
}
