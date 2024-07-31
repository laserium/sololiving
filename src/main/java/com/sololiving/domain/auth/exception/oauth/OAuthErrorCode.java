package com.sololiving.domain.auth.exception.oauth;

import com.sololiving.global.exception.error.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OAuthErrorCode implements ErrorCode {

    FAIL_TO_RETRIVE_KAKAO_TOKEN("OAUTH_E001", "카카오 로그인 관련 토큰을 회수할 수 없습니다."),
    MISSING_OAUTH2_CONFIGURATION_PROPERTIES("OAUTH_E002", "OAUTH2 관련 정보를 찾을 수 없습니다."),
    FAIL_TO_RETRIEVE_USER_INFO("OAUTH_E003", "사용자 정보 조회 실패(OAuth2.0, NAVER)");
    

    private final String code;
    private final String message;

}
