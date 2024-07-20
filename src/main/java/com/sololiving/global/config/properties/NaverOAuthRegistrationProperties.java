package com.sololiving.global.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.naver")
@Getter
@Setter
public class NaverOAuthRegistrationProperties {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String authorizationGrantType;
}
