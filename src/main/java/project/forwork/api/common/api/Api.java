package project.forwork.api.common.api;

import jakarta.validation.Valid;
import lombok.Data;
import project.forwork.api.common.error.ErrorCodeIfs;

import java.util.Map;

@Data
public class Api<T> {

    private Result result;

    @Valid
    private T body;

    public static <T> Api<T> OK(T data){
        Api<T> api = new Api<>();
        api.result = Result.OK();
        api.body = data;
        return api;
    }

    public static <T> Api<T> CREATED(T data){
        Api<T> api = new Api<>();
        api.result = Result.CREATED();
        api.body = data;
        return api;
    }

    public static <T> Api<T> FOUND(T data){
        Api<T> api = new Api<>();
        api.result = Result.FOUND();
        api.body = data;
        return api;
    }

    public static Api<Object> ERROR(ErrorCodeIfs errorCodeIfs){
        Api<Object> api = new Api<Object>();
        api.result = Result.ERROR(errorCodeIfs);
        return api;
    }

    public static Api<Object> ERROR(ErrorCodeIfs errorCodeIfs, Throwable tx){
        Api<Object> api = new Api<Object>();
        api.result = Result.ERROR(errorCodeIfs, tx);
        return api;
    }

    public static Api<Object> ERROR(ErrorCodeIfs errorCodeIfs, String description){
        Api<Object> api = new Api<Object>();
        api.result = Result.ERROR(errorCodeIfs, description);
        return api;
    }

    public static <T> Api<T> ERROR(Integer resultCode, String resultMessage, String resultDescription) {
        Api<T> api = new Api<>();
        api.result = Result.ERROR(resultCode, resultMessage, resultDescription);
        return api;
    }
}
