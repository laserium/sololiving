package com.sololiving.domain.auth.exception;

import com.sololiving.global.exception.success.SuccessCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthSuccessCode implements SuccessCode {

    SIGN_UP_SUCCESS("AUTH_S001", "회원가입에 성공하셨습니다."),
    SIGN_OUT_SUCCESS("AUTH_S002", "성공적으로 로그아웃 하셨습니다."),
    OAUTH2_SUCCESS("AUTH_S003", "OAuth2.0 인증 완료"),
    PASSWORD_RESET_SUCCESS("AUTH_S004", "비밀번호 초기화 - 임시 비밀번호 설정 메일 전송 완료"),
    ID_RECOVER_SUCCESS("AUTH_S005", "아이디 찾기 - 메일 전송 완료");

    private final String code;
    private final String message;
    
}
