package com.shiyi.mallchat.common.exception;


import com.shiyi.mallchat.common.domain.enums.CommonErrorEnum;
import com.shiyi.mallchat.common.domain.vo.resp.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value= MethodArgumentNotValidException.class)
    public ApiResult<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        StringBuilder errorMsg = new StringBuilder();
        e.getBindingResult().getFieldErrors().forEach(error -> errorMsg.append(error.getField()).append(":").append(error.getDefaultMessage()).append(";"));
        String msg = errorMsg.toString();
        return ApiResult.fail(CommonErrorEnum.PARAM_INVALID.getCode(), msg.substring(0, msg.length() - 1));
    }

    @ExceptionHandler(value= BusinessException.class)
    public ApiResult<?> handleException(BusinessException e) {
        log.error("business error:{}", e.getMessage(), e);
        return ApiResult.fail(e.getErrorError(), e.getErrorMessage());
    }

    @ExceptionHandler(value= Exception.class)
    public ApiResult<?> handleException(Exception e) {
        log.error("error:{}", e.getMessage(), e);
        return ApiResult.fail(CommonErrorEnum.SYSTEM_ERROR);
    }
}
