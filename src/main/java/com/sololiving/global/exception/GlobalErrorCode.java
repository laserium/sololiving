package com.sololiving.global.exception;

import com.sololiving.global.exception.error.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GlobalErrorCode implements ErrorCode {
    USER_NOT_FOUND("GLOB001", "알맞은 사용자를 찾을 수 없습니다.");

    private final String code;
    private final String message;

}
