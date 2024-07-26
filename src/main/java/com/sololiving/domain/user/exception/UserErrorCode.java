package com.sololiving.domain.user.exception;

import com.sololiving.global.exception.error.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode{
    USER_NOT_FOUND("USER_E001", "해당하는 유저정보를 찾을 수 없음");

    private final String code;
    private final String message;

}
