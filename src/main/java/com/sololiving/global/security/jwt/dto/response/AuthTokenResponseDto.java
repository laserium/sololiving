package com.sololiving.global.security.jwt.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthTokenResponseDto {
    private String accessToken;
    private String refreshToken;
}
