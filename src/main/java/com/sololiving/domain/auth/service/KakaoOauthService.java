package com.sololiving.domain.auth.service;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.sololiving.domain.auth.dto.oauth.response.KakaoTokenResponseDto;
import com.sololiving.domain.auth.exception.AuthErrorCode;
import com.sololiving.global.config.properties.KakaoOAuthProviderProperties;
import com.sololiving.global.config.properties.KakaoOAuthRegistrationProperties;
import com.sololiving.global.exception.Exception;

import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class KakaoOAuthService {


    private final KakaoOAuthRegistrationProperties kakaoOAuthRegistrationProperties;
    private final KakaoOAuthProviderProperties kakaoOAuthProviderProperties;

    public String getKakaoToken(String authCode) {
        WebClient webClient = WebClient.builder()
                .baseUrl(kakaoOAuthProviderProperties.getTokenUri())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .build();
        KakaoTokenResponseDto kakaoTokenResponseDto = webClient.post()
                .uri(uriBuilder -> uriBuilder.build())
                .body(BodyInserters.fromFormData("grant_type", kakaoOAuthRegistrationProperties.getAuthorizationGrantType())
                        .with("client_id", kakaoOAuthRegistrationProperties.getClientId())
                        .with("redirect_uri", kakaoOAuthRegistrationProperties.getRedirectUri())
                        .with("code", authCode)
                        .with("client_secret", kakaoOAuthRegistrationProperties.getClientSecret()))
                .retrieve()
                .bodyToMono(KakaoTokenResponseDto.class)
                .block();
        if (kakaoTokenResponseDto != null) {
            log.info(" [Kakao Service] Access Token ------> {}", kakaoTokenResponseDto.getAccessToken());
            log.info(" [Kakao Service] Refresh Token ------> {}", kakaoTokenResponseDto.getRefreshToken());
            log.info(" [Kakao Service] Id Token ------> {}", kakaoTokenResponseDto.getIdToken());
            log.info(" [Kakao Service] Scope ------> {}", kakaoTokenResponseDto.getScope());
            return kakaoTokenResponseDto.getAccessToken();
        } else {
            throw new Exception(AuthErrorCode.FAIL_TO_RETRIVE_KAKAO_TOKEN);
        }
    }
}
