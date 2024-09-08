package project.forwork.api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SalePostErrorCode implements ErrorCodeIfs{

    SALE_POST_NOT_FOUND(404, 4401, "이력서 판매글을 찾을 수 없습니다."),
    ACCESS_NOT_PERMISSION(403, 4402, "이력서 판매글 접근 권한이 없습니다."),
    RESUME_NOT_SELLING(404, 4402, "판매중인 이력서 게시글이 아닙니다."),
    ;

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;
}
