package com.sololiving.domain.user.exception;

public enum UserErrorCode {
    USER_NOT_FOUND("U001", "해당하는 유저정보를 찾을 수 없음");

    private final String code;
    private final String message;

    UserErrorCode(String code, String message) {
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
