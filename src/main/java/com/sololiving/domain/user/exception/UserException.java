package com.sololiving.domain.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class UserException extends RuntimeException {
    private final UserErrorCode errorCode;

    public UserException(UserErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public UserErrorCode getErrorCode() {
        return errorCode;
    }
}