package com.sololiving.domain.auth.dto;

import java.time.Duration;

import lombok.Builder;
import lombok.Getter;

public class TokenDto {
    
    @Getter
    @Builder
    public static class CreateTokensResponse {

        private String refreshToken;
        private String accessToken;
        private Duration expiresIn;

    }
}
