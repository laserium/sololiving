package com.sololiving.domain.user.exception;

import com.sololiving.global.exception.error.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode{
    USER_NOT_FOUND("USER_E001", "해당하는 유저정보를 찾을 수 없음"),
    USER_EMAIL_NOT_FOUND("USER_E002", "해당하는 유저의 이메일을 찾을 수 없음"),
    USER_ID_NOT_FOUND("USER_E003", "해당하는 유저의 아이디를 찾을 수 없음"),
    USER_PWD_NOT_FOUND("USER_E004", "해당하는 유저의 비밀번호를 찾을 수 없음"),
    USER_ID_INCORRECT("USER_E005", "해당하는 유저의 아이디가 일치하지 않음"),
    USER_TYPE_ERROR_NO_PERMISSION("USER_E006", "해당하는 유저는 권한이 없습니다.");

    private final String code;
    private final String message;

}
