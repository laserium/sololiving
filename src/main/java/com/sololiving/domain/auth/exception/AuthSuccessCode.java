package com.sololiving.domain.auth.exception;

import com.sololiving.global.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthSuccessCode implements ErrorCode {

    SIGN_UP_SUCCESS("AUTH_S001", "회원가입에 성공하셨습니다.");

    private final String code;
    private final String message;
    
}
