package project.forwork.api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ThumbnailImageErrorCode implements ErrorCodeIfs {

    NOT_FOUND(404, 11401, "해당 썸네일 이미지가 존재 하지 않습니다"),
    ;

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;
}
