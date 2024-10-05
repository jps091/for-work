package project.forwork.api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderErrorCode implements ErrorCodeIfs{

    ORDER_NOT_FOUND(404, 8401, "주문 정보를 찾을 수 없습니다."),
    ORDER_NOT_PERMISSION(403, 8402, "주문에 대한 권한이 없습니다."),
    RESUME_ALREADY_SEND(400, 8403, "이미 구매 확정인 주문입니다."),
    ORDER_ALREADY_REQUEST(400, 8404, "이미 요청한 주문 입니다."),
    ORDER_ABNORMAL(400, 8405, "비정상 주문 입니다. 다시 시도 해주세요."),
    ;

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;
}
