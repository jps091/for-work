package project.forwork.api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TransactionErrorCode implements ErrorCodeIfs {

    TX_AlREADY_PAYMENT(400, 10401, "이미 결제된 거래 입니다."),
    TX_NOT_FOUND(404, 10402, "결제 내역이 존재 하지 않습니다"),
    TX_SERVER_ERROR(500, 10403, "시스템 오류입니다. 잠시후 다시 시도 해주세요."),
    TX_CLIENT_ERROR(400, 10404, "결제를 실패 하였습니다. 결제 관련 정보를 확인후 다시 시도 해주세요."),
    ;

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;
}
