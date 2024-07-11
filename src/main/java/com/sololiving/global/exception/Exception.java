package com.sololiving.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
@Getter
public class Exception extends RuntimeException {

    private final ErrorCode errorCode;

    public Exception(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}