package project.forwork.api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum S3ErrorCode implements ErrorCodeIfs {

    S3_ERROR(500, 12401, "서버 문제로 요청이 실패 하였습니다. 잠시후 다시 시도 해주세요."),
    AWS_SDK_ERROR(500, 12402, "서버 문제로 요청이 실패 하였습니다. 잠시후 다시 시도 해주세요."),
    IO_ERROR(500, 12403, "서버 문제로 요청이 실패 하였습니다. 잠시후 다시 시도 해주세요."),
    S3_BUCKET_INCORRECT(500, 12404, "서버 문제로 요청이 실패 하였습니다. 잠시후 다시 시도 해주세요."),
    NOT_FOUND_FILE(500, 12405, "서버 문제로 요청이 실패 하였습니다. 잠시후 다시 시도 해주세요."),
    ALREADY_REQUEST_IMAGE(400, 12406, "이미 요청한 파일 입니다. 확인 해주세요."),
    VALID_FILE_FORMAT(400, 12407, "업로드 파일확장자 jpg, png, jpeg 만 가능 합니다. 확인 해주세요."),
    ;

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;
}