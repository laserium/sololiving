package com.sololiving.domain.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class AuthException extends RuntimeException {
    private final AuthErrorCode errorCode;

    public AuthException(AuthErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public AuthErrorCode getErrorCode() {
        return errorCode;
    }
}