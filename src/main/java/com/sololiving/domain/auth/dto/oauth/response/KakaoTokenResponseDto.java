package com.sololiving.domain.auth.dto.oauth.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;

// 카카오 로그인 - 토큰 발급 ResponseDto
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoTokenResponseDto {
    @JsonProperty("token_type")
    public String tokenType;

    @JsonProperty("access_token")
    public String accessToken;

    @JsonProperty("id_token")
    public String idToken;

    @JsonProperty("expires_in")
    public Integer expiresIn;

    @JsonProperty("refresh_token")
    public String refreshToken;

    @JsonProperty("refresh_token_expires_in")
    public Integer refreshTokenExpiresIn;
    
    @JsonProperty("scope")
    public String scope;
}