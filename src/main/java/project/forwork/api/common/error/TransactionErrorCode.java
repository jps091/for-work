package project.forwork.api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TransactionErrorCode implements ErrorCodeIfs {

    TX_AlREADY_PAYMENT(400, 9401, "이미 결제된 거래 입니다."),
    TX_NOT_FOUND(404, 9402, "결제 내역이 존재 하지 않습니다"),
    ;

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;
}
