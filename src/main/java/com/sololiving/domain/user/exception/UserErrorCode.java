package com.sololiving.domain.user.exception;

import com.sololiving.global.exception.error.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
    ID_ALREADY_EXISTS("USER_E001", "아이디가 이미 존재합니다."),
    EMAIL_ALREADY_EXISTS("USER_E002", "이메일이 이미 존재합니다."),
    CONTACT_ALREADY_EXISTS("USER_E003", "연락처가 이미 존재합니다."),
    USER_NOT_FOUND("USER_E004", "해당하는 유저정보를 찾을 수 없음"),
    USER_EMAIL_NOT_FOUND("USER_E005", "해당하는 유저의 이메일을 찾을 수 없음"),
    USER_ID_NOT_FOUND("USER_E006", "해당하는 유저의 아이디를 찾을 수 없음"),
    USER_PWD_NOT_FOUND("USER_E007", "해당하는 유저의 비밀번호를 찾을 수 없음"),
    USER_ID_INCORRECT("USER_E008", "해당하는 유저의 아이디가 일치하지 않음"),
    USER_TYPE_ERROR_NO_PERMISSION("USER_E009", "해당하는 유저는 권한이 없습니다."),
    NO_USER_ID_REQUEST("USER_E010", "유저아이디 값이 정확하게 요청되지 않았습니다."),
    NO_USER_STATUS_REQUEST("USER_E011", "유저 상태 값이 정확하게 요청되지 않았습니다."),
    UPDATE_USER_REQUEST_DATA_IS_NULL("USER_E012", "유저 정보 수정 요청 데이터 값이 없습니다(NULL)");

    private final String code;
    private final String message;

}
