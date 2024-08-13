package com.sololiving.domain.auth.exception.auth;

import com.sololiving.global.exception.error.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    PASSWORD_INCORRECT("AUTH_E001", "비밀번호가 일치하지 않습니다."),
    FAIL_TO_RETRIVE_KAKAO_TOKEN("AUTH_E002", "카카오 로그인 관련 토큰을 회수할 수 없습니다."),
    MISSING_OAUTH2_CONFIGURATION_PROPERTIES("AUTH_E003", "OAUTH2 관련 정보를 찾을 수 없습니다."),
    FAIL_TO_RETRIEVE_USER_INFO("AUTH_E004", "사용자 정보 조회 실패(OAuth2.0, NAVER)"),
    WRONG_PARAMETER_OR_REQUEST("AUTH_E005", "파라미터가 잘못되었거나 요청문이 잘못되었습니다."),
    CANNOT_SIGN_OUT("AUTH_E006", "로그아웃을 할 수 없습니다.");

    private final String code;
    private final String message;

}
