package com.sololiving.domain.auth.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
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

        if (clientId == null || clientSecret == null || redirectUri == null || tokenUri == null) {
            throw new IllegalArgumentException("Missing OAuth2 configuration properties");
        }
    }

    public void getKakaoAuthCode() {
        RestTemplate restTemplate = new RestTemplate();
        String requestUrl = authorizationUri
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&response_type=code";
        restTemplate.getForEntity(requestUrl, Object.class);
    }

    public String getKakaoToken(String authcode) {
        KakaoTokenResponseDto kakaoTokenResponseDto = WebClient.create().post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path(tokenUri)
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", clientId)
                        .queryParam("code", authcode)
                        .build(true))
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(KakaoTokenResponseDto.class)
                .block();


        log.info(" [Kakao Service] Access Token ------> {}", kakaoTokenResponseDto.getAccessToken());
        log.info(" [Kakao Service] Refresh Token ------> {}", kakaoTokenResponseDto.getRefreshToken());
        //제공 조건: OpenID Connect가 활성화 된 앱의 토큰 발급 요청인 경우 또는 scope에 openid를 포함한 추가 항목 동의 받기 요청을 거친 토큰 발급 요청인 경우
        log.info(" [Kakao Service] Id Token ------> {}", kakaoTokenResponseDto.getIdToken());
        log.info(" [Kakao Service] Scope ------> {}", kakaoTokenResponseDto.getScope());

        return kakaoTokenResponseDto.getAccessToken();
    }
}
