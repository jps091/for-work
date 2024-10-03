package project.forwork.api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import project.forwork.api.common.error.ErrorCodeIfs;

@AllArgsConstructor
@Getter
public enum WalletErrorCode implements ErrorCodeIfs {

    WALLET_EXIST(400, 9401, "이미 지갑이 존재 합니다."),
    WALLET_NOT_EXIST(404, 9402, "이미 지갑이 존재 합니다."),
    BALANCE_EXCEED(400, 9403, "충전금액 한도를 초과 했습니다."),
    BALANCE_NOT_ENOUGH(400, 9404, "잔액이 부족 합니다."),
    ;

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;
}
