package project.forwork.api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderResumeErrorCode implements ErrorCodeIfs{

    NOT_FOUND(404, 9401, "주문 이력서 정보를 찾을 수 없습니다."),
    STATUS_NOT_CONFIRM(400, 9402, "주문 이력서가 구매확정이 아니라 메일을 전송 할 수 없습니다."),
    STATUS_CANCEL(400, 9403, "주문 이력서가 취소된 상태입니다."),
    CANCEL_FAIL(400, 9404, "주문 이력서가 결제완료 상태가 아니기 때문에 취소 할 수 없습니다."),
    NOT_SELECTED(400, 9405, "선택 하신 이력서는 구매상태가 아니기 때문에 주문 취소를 할 수 없습니다."),
    ;

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;
}
