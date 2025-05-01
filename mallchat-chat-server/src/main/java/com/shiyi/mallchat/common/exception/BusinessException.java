package com.shiyi.mallchat.common.exception;


import com.shiyi.mallchat.common.domain.enums.CommonErrorEnum;
import lombok.Data;

@Data
public class BusinessException extends RuntimeException {

    protected Integer errorError;

    protected String errorMessage;


    public BusinessException(String message) {
        super(message);
        this.errorError = CommonErrorEnum.BUSINESS_ERROR.getCode();
        this.errorMessage = message;
    }

    public BusinessException(Integer errorError, String errorMessage) {
        super(errorMessage);
        this.errorError = errorError;
        this.errorMessage = errorMessage;
    }

    public BusinessException(CommonErrorEnum commonErrorEnum) {
        super(commonErrorEnum.getMsg());
        this.errorError = commonErrorEnum.getCode();
        this.errorMessage = commonErrorEnum.getMsg();
    }
}
