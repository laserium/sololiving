package com.sololiving.domain.auth.dto;

import java.time.Duration;

import lombok.Builder;
import lombok.Getter;

public class TokenDto {

    // 토큰 생성 ResponseDto
    @Getter
    @Builder
    public static class CreateTokensResponse {

        private String refreshToken;
        private String accessToken;
        private Duration expiresIn;

    }
}
