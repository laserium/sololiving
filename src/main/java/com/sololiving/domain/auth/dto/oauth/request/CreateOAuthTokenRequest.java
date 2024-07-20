package com.sololiving.domain.auth.dto.oauth.request;

import lombok.Getter;

@Getter
public class CreateOAuthTokenRequest {
    
    private String authCode;
}
