package com.sololiving.domain.auth.dto.token.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthTokenResponseDto {
    private String accessToken;
    private String refreshToken;
}
