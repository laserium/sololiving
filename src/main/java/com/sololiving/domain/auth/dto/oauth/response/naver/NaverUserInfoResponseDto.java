package com.sololiving.domain.auth.dto.oauth.response.naver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverUserInfoResponseDto {

    @JsonProperty("resultcode")
    private String resultCode;

    @JsonProperty("message")
    private String message;

    @JsonProperty("response")
    private Response response;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response {
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

        @JsonProperty("profile_image")
        private String profileImage;
    }
}