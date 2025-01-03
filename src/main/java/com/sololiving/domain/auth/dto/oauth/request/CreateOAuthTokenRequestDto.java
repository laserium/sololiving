package com.sololiving.domain.auth.dto.oauth.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateOAuthTokenRequestDto {
    private String authCode;
}
