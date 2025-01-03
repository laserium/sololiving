package com.sololiving.domain.auth.dto.oauth.response.google;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

// 구글 로그인 - 토큰 발급 ResponseDto
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleTokenResponseDto {

    @JsonProperty("access_token")
    public String accessToken;

    @JsonProperty("refresh_token")
    public String refreshToken;

    @JsonProperty("scope")
    public String scope;

    @JsonProperty("token_type")
    public String tokenType;

    @JsonProperty("expires_in")
    public Integer expiresIn;


}
