package project.forwork.api.common.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.forwork.api.common.error.ErrorCode;
import project.forwork.api.common.error.ErrorCodeIfs;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Result {

    private Integer resultCode;
    private String resultMessage;

    private String resultDescription;

    public static Result OK(){
        return Result.builder()
                .resultCode(ErrorCode.OK.getErrorCode())
                .resultMessage(ErrorCode.OK.getDescription())
                .resultDescription("성공")
                .build();
    }

    public static Result CREATED(){
        return Result.builder()
                .resultCode(ErrorCode.CREATED.getErrorCode())
                .resultMessage(ErrorCode.CREATED.getDescription())
                .resultDescription("성공")
                .build();
    }

    public static Result FOUND(){
        return Result.builder()
                .resultCode(ErrorCode.FOUND.getErrorCode())
                .resultMessage(ErrorCode.FOUND.getDescription())
                .resultDescription("성공")
                .build();
    }

    public static Result ERROR(ErrorCodeIfs errorCodeIfs){
        return Result.builder()
                .resultCode(errorCodeIfs.getErrorCode())
                .resultMessage(errorCodeIfs.getDescription())
                .resultDescription("실패")
                .build();
    }

    public static Result ERROR(ErrorCodeIfs errorCodeIfs, Throwable tx){
        return Result.builder()
                .resultCode(errorCodeIfs.getErrorCode())
                .resultMessage(errorCodeIfs.getDescription())
                .resultDescription(tx.getLocalizedMessage())
                .build();
    }

    public static Result ERROR(ErrorCodeIfs errorCodeIfs, String description){
        return Result.builder()
                .resultCode(errorCodeIfs.getErrorCode())
                .resultMessage(errorCodeIfs.getDescription())
                .resultDescription(description)
                .build();
    }

    public static Result ERROR(Integer resultCode, String resultMessage, String resultDescription) {
        return Result.builder()
                .resultCode(resultCode)
                .resultMessage(resultMessage)
                .resultDescription(resultDescription)
                .build();
    }
}
