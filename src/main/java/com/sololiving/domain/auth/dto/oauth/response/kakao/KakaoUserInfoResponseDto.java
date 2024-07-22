package com.sololiving.domain.auth.dto.oauth.response.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoUserInfoResponseDto {

    @JsonProperty("id")
    private Long id;


}