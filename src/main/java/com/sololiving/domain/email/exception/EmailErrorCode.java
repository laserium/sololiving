package com.sololiving.domain.email.exception;

import com.sololiving.global.exception.error.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmailErrorCode implements ErrorCode {

    TOKEN_NOT_FOUND("EMAIL_E001", "해당하는 이메일 인증 토큰을 찾을 수 없습니다.");

    private final String code;
    private final String message;

}
