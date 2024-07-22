package com.sololiving.domain.auth.dto.oauth.response.naver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverRequestTokenRefreshDto {
    @JsonProperty("access_token")
    public String accessToken;

    @JsonProperty("token_type")
    public String tokenType;

    @JsonProperty("expires_in")
    public Integer expiresIn;

    @JsonProperty("error")
    public String error;

    @JsonProperty("error_description")
    public String errorDescription;
}
