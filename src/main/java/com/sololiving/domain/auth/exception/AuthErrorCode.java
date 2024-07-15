package com.sololiving.domain.auth.exception;

public enum AuthErrorCode {
    ID_ALREADY_EXISTS("AUTH001", "아이디가 이미 존재합니다."),
    EMAIL_ALREADY_EXISTS("AUTH002", "이메일이 이미 존재합니다."),
    CONTACT_ALREADY_EXISTS("AUTH003", "연락처가 이미 존재합니다."),
    PASSWORD_INCORRECT("AUTH004", "비밀번호가 틀립니다.");

    private final String code;
    private final String message;

    AuthErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
