package project.forwork.api.common.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.forwork.api.common.api.Api;
import project.forwork.api.common.error.ErrorCodeIfs;
import project.forwork.api.common.exception.ApiException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
@Order(value = Integer.MIN_VALUE + 1)
public class MethodArgumentNotValidExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Api<Object>> handleValidationExceptions(MethodArgumentNotValidException ex){
        log.error("MethodArgumentNotValidException ex", ex);
        StringBuilder errorMessages = new StringBuilder();

        /*** TODO
         *     {
         *         "objectName": "resumeRegisterRequest",
         *         "field": "price",
         *         "rejectedValue": 100100,
         *         "defaultMessage": "최대 판매 가격은 100,000원 이하이어야 합니다."
         *     }
         */

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errorMessages.append(fieldName).append(": ").append(errorMessage).append("; ");
        });

        Api<Object> apiErrorResponse = Api.ERROR(
                400,
                "잘못된 요청",
                errorMessages.toString().trim()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiErrorResponse);
    }
}

