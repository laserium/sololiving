package com.sololiving.domain.auth.dto.oauth.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateOAuthTokenRequestDto {
    private String authCode;
}
