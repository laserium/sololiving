package com.sololiving.global.exception.success;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@ResponseStatus(code = HttpStatus.OK)
@Getter
public class SuccessException extends RuntimeException {

    private final SuccessCode successCode;

    public SuccessException(SuccessCode successCode) {
        super(successCode.getMessage());
        this.successCode = successCode;
    }
}