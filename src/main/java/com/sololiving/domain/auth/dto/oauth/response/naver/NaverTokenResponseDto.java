package com.sololiving.domain.auth.dto.oauth.response.naver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
// 네이버 로그인 - 토큰 발급 ResponseDto
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverTokenResponseDto {

    @JsonProperty("access_token")
    public String accessToken;

    @JsonProperty("refresh_token")
    public String refreshToken;

    @JsonProperty("token_type")
    public String tokenType;

    @JsonProperty("expires_in")
    public String expiresIn;

    @JsonProperty("error")
    public String error;

    @JsonProperty("error_description")
    public String errorDescription;

}
