package project.forwork.api.domain.retrylog.infrastructure.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum RetryType {

    CONFIRM,
    CANCEL,
    PARTIAL_CANCEL
    ;
    @JsonCreator
    public static RetryType from(String s) {
        for (RetryType status : RetryType.values()) {
            if (status.name().equalsIgnoreCase(s)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid RetryRequestType: " + s);
    }
}
