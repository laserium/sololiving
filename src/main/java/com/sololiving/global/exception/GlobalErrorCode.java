package com.sololiving.global.exception;

import com.sololiving.global.exception.error.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GlobalErrorCode implements ErrorCode {
    ERROR("GLOB_E001", "에러"),
    NO_PERMISSION("GLOB_E001", "권한이 없습니다.");

    private final String code;
    private final String message;

}
