package project.forwork.api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TransactionErrorCode implements ErrorCodeIfs {

    TX_AlREADY_CHARGE(400, 9401, "이미 충전된 거래 입니다."),
    TX_AlREADY_PAYMENT(400, 9401, "이미 결제된 거래 입니다."),
    ;

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;
}
