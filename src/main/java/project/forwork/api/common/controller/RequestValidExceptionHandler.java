package project.forwork.api.common.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import project.forwork.api.common.api.Api;
import project.forwork.api.common.error.ErrorCode;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
@Order(value = Integer.MIN_VALUE + 1)
public class RequestValidExceptionHandler {

    @ExceptionHandler({HandlerMethodValidationException.class})
    public ResponseEntity<Api<Object>> handleValidationExceptions(HandlerMethodValidationException ex){
        String message = ex.getAllValidationResults().stream()
                .map(ParameterValidationResult::getResolvableErrors)
                .flatMap(List::stream)
                .map(MessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Api.ERROR(ErrorCode.BAD_REQUEST, message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Api<Object>> methodArgumentNotValidExceptions(MethodArgumentNotValidException ex){
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Api.ERROR(ErrorCode.BAD_REQUEST, message));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Api<Object>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String message = "JSON 요청이 잘못되었습니다. Enum 값이 유효하지 않습니다.";
        return ResponseEntity.badRequest().body(Api.ERROR(ErrorCode.BAD_REQUEST, message));
    }
}

