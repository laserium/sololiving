package com.sololiving.domain.auth.exception;

import com.sololiving.global.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    ID_ALREADY_EXISTS("AUTH001", "아이디가 이미 존재합니다."),
    EMAIL_ALREADY_EXISTS("AUTH002", "이메일이 이미 존재합니다."),
    CONTACT_ALREADY_EXISTS("AUTH003", "연락처가 이미 존재합니다."),
    INVALID_PASSWORD("AUTH004", "비밀번호가 일치하지 않습니다.");

    private final String code;
    private final String message;

}
