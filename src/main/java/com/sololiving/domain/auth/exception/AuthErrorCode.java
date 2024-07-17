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
    PASSWORD_INCORRECT("AUTH004", "비밀번호가 일치하지 않습니다."),
    CANNOT_FIND_RT("AUTH005", "해당하는 유저의 RefreshToken을 찾을 수 없습니다."),
    FAIL_TO_RETRIVE_KAKAO_TOKEN("AUTH006", "카카오 로그인 관련 토큰을 회수할 수 없습니다."),
    MISSING_OAUTH2_CONFIGURATION_PROPERTIES("AUTH007", "OAUTH2 관련 정보를 찾을 수 없습니다.");

    private final String code;
    private final String message;

}
