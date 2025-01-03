package com.sololiving.domain.auth.exception.oauth;

import com.sololiving.global.exception.success.SuccessCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OAuthSuccessCode implements SuccessCode {

    OAUTH2_SUCCESS("OAUTH_S001", "OAuth2.0 인증 완료");

    private final String code;
    private final String message;
    
}
