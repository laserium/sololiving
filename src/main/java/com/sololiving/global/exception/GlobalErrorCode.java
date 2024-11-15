package com.sololiving.global.exception;

import com.sololiving.global.exception.error.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GlobalErrorCode implements ErrorCode {
    ERROR("GLOB_E001", "에러"),
    NO_PERMISSION("GLOB_E002", "권한이 없습니다."),
    REQUEST_IS_NULL("GLOB_E003", "요청 데이터가 NULL 입니다"),
    REQUEST_TYPE_IS_WRONG("GLOB_E004", "요청 값이 유효하지 않습니다");

    private final String code;
    private final String message;

}
