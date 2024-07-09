package com.sololiving.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class CustomException extends RuntimeException {
    private final GlobalErrorCode errorCode;

    public CustomException(GlobalErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public GlobalErrorCode getErrorCode() {
        return errorCode;
    }
}