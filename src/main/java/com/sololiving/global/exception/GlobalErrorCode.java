package com.sololiving.global.exception;

import com.sololiving.global.exception.error.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GlobalErrorCode implements ErrorCode {
    ERROR("GLOB001", "에러");

    private final String code;
    private final String message;

}
