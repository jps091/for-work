package project.forwork.api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SalesPostErrorCode implements ErrorCodeIfs{

    SALES_POST_NOT_FOUND(404, 5401, "이력서 판매글을 찾을 수 없습니다."),
    ACCESS_NOT_PERMISSION(403, 5402, "이력서 판매글 접근 권한이 없습니다."),
    RESUME_NOT_SELLING(404, 5402, "판매중인 이력서 게시글이 아닙니다."),
    SALES_POST_NO_CONTENT(204, 3405, "컨텐츠가 존재 하지 않습니다."),
    ;

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;
}