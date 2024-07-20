package com.sololiving.domain.auth.dto.oauth.response;

import com.sololiving.domain.auth.enums.ClientId;

import lombok.Getter;

// 카카오 로그인 - 토큰 인증 ResponseDto
@Getter
public class KakaoTokenValidationResponseDto {
    private String accessToken;
    private ClientId clientId;
}