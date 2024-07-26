package com.sololiving.domain.auth.exception;

import com.sololiving.global.exception.success.SuccessCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthSuccessCode implements SuccessCode {

    SIGN_UP_SUCCESS("AUTH_S001", "회원가입에 성공하셨습니다."),
    SIGN_OUT_SUCCESS("AUTH_S002", "성공적으로 로그아웃 하셨습니다."),
    OAUTH2_SUCCESS("AUTH_S003", "OAuth2.0 인증 완료");

    private final String code;
    private final String message;
    
}
