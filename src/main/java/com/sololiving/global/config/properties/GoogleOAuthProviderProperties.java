package com.sololiving.global.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "spring.security.oauth2.client.provider.google")
@Getter
@Setter
public class GoogleOAuthProviderProperties {

    private String tokenUri;
    private String authorizationUri;
}
