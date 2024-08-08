package com.sololiving.domain.auth.dto.oauth.request;

import lombok.Getter;

@Getter
public class CreateOAuthTokenRequestDto {
    private String authCode;
}
