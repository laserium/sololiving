package com.sololiving.domain.log.exception;

import com.sololiving.global.exception.error.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LogErrorCode implements ErrorCode {

    FAIL_TO_UPLOAD_FILE("LOG_E001", "");

    private final String code;
    private final String message;

}
