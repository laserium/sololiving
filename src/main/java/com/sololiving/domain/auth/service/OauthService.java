package com.sololiving.domain.auth.service;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.sololiving.domain.auth.dto.KakaoTokenResponseDto;

import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class OauthService {

    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final String authorizationUri;
    private final String tokenUri;

    public OauthService(Environment env) {
        this.clientId = env.getProperty("spring.security.oauth2.client.registration.kakao.client-id", "");
        this.clientSecret = env.getProperty("spring.security.oauth2.client.registration.kakao.client-secret", "");
        this.redirectUri = env.getProperty("spring.security.oauth2.client.registration.kakao.redirect-uri", "");
        this.authorizationUri = env.getProperty("spring.security.oauth2.client.provider.kakao.authorization-uri", "");
        this.tokenUri = env.getProperty("spring.security.oauth2.client.provider.kakao.token-uri", "");
        
        if (clientId.isEmpty() || clientSecret.isEmpty() || redirectUri.isEmpty() || tokenUri.isEmpty()) {
            throw new IllegalArgumentException("Missing OAuth2 configuration properties");
        }
    }

    public String getKakaoToken(String authCode) {
        WebClient webClient = WebClient.builder()
                .baseUrl(tokenUri)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .build();

        KakaoTokenResponseDto kakaoTokenResponseDto = webClient.post()
                .uri(uriBuilder -> uriBuilder.build())
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", clientId)
                        .with("redirect_uri", redirectUri)
                        .with("code", authCode)
                        .with("client_secret", clientSecret))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(KakaoTokenResponseDto.class)
                .block();

        if (kakaoTokenResponseDto != null) {
            log.info(" [Kakao Service] Access Token ------> {}", kakaoTokenResponseDto.getAccessToken());
            log.info(" [Kakao Service] Refresh Token ------> {}", kakaoTokenResponseDto.getRefreshToken());
            log.info(" [Kakao Service] Id Token ------> {}", kakaoTokenResponseDto.getIdToken());
            log.info(" [Kakao Service] Scope ------> {}", kakaoTokenResponseDto.getScope());
            return kakaoTokenResponseDto.getAccessToken();
        } else {
            throw new RuntimeException("Failed to retrieve Kakao token");
        }
    }
}
