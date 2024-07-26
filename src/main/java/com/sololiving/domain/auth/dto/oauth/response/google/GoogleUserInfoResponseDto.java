package com.sololiving.domain.auth.dto.oauth.response.google;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleUserInfoResponseDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("email")
    private String email;

    @JsonProperty("verified_email")
    private boolean verifiedEmail;

    @JsonProperty("name")
    private String name;

    @JsonProperty("given_name")
    private String givenName;

    @JsonProperty("family_name")
    private String familyName;

    @JsonProperty("picture")
    private String picture;

    @JsonProperty("locale")
    private String locale;
}
