package com.sololiving.domain.auth.dto.oauth.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverUserInfoResponseDto {
    
    @JsonProperty("resultcode")
    private String resultCode;

    @JsonProperty("message")
    private String message;

    @JsonProperty("id")
    private String id;

    @JsonProperty("age")
    private String age;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("email")
    private String email;

    @JsonProperty("mobile")
    private String mobile;

    @JsonProperty("name")
    private String name;

    @JsonProperty("birthday")
    private String birthday;

    @JsonProperty("birthyear")
    private String birthyear;

}
