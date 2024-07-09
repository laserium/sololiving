package com.sololiving.domain.user.exception;

public enum UserErrorCode {
    USER_ERROR("U001", "에러내용입력");

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
