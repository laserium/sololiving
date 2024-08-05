package com.sololiving.global.security.jwt.dto.response;

import java.time.Duration;

import lombok.Builder;
import lombok.Getter;

// 토큰 생성 ResponseDto
@Getter
@Builder
public class CreateTokenResponse {

    private String refreshToken;
    private String accessToken;
    private Duration expiresIn;

}