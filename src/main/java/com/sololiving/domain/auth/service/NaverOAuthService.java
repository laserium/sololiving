package com.sololiving.domain.auth.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sololiving.domain.auth.dto.auth.request.SignInRequestDto;
import com.sololiving.domain.auth.dto.oauth.response.NaverRequestTokenRefreshDto;
import com.sololiving.domain.auth.dto.oauth.response.NaverTokenResponseDto;
import com.sololiving.domain.auth.dto.oauth.response.NaverUserInfoResponseDto;
import com.sololiving.domain.auth.dto.oauth.response.OauthUserExistenceResponseDto;
import com.sololiving.domain.auth.enums.ClientId;
import com.sololiving.domain.auth.exception.AuthErrorCode;
import com.sololiving.domain.user.mapper.UserMapper;
import com.sololiving.domain.vo.UserVo;
import com.sololiving.global.config.properties.NaverOAuthRegistrationProperties;
import com.sololiving.global.exception.Exception;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverOAuthService {

    private final NaverOAuthRegistrationProperties naverOAuthRegistrationProperties;
    private final UserMapper userMapper;
    private final AuthService authService;
    private final WebClient.Builder webClientBuilder;

    private static final String NAVER_ID_PREFIX = "NAVER_";

    @Value("${naver.oauth2.state}")
    private String state;

    public OauthUserExistenceResponseDto checkOauthUserExistence(String authCode) {
        String oauth2UserId = NAVER_ID_PREFIX + getUserInfoByToken(getTokenByCode(authCode)).getResponse().getId();
        log.info(oauth2UserId);
        Optional<UserVo> userOptional = userMapper.findByOauth2UserId(oauth2UserId);
        if (userOptional.isPresent()) {
            SignInRequestDto signInRequestDto = SignInRequestDto.builder()
                                                .userId(userOptional.get().getUserId())
                                                .userPwd(null)
                                                .clientId(ClientId.NAVER)
                                                .build();
            authService.signIn(signInRequestDto);
            return OauthUserExistenceResponseDto.builder().isOauthUser("true").oauth2UserId(oauth2UserId).build();
        } else {
            return OauthUserExistenceResponseDto.builder().isOauthUser("false").oauth2UserId(oauth2UserId).build();
        }
        
    }

    private String getTokenByCode(String authCode) {
        String encodedState = encodeState(state);
        WebClient webClient = webClientBuilder.build();
        NaverTokenResponseDto naverTokenResponseDto = webClient.post()
                .uri("https://nid.naver.com/oauth2.0/token")
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", naverOAuthRegistrationProperties.getClientId())
                        .with("client_secret", naverOAuthRegistrationProperties.getClientSecret())
                        .with("code", authCode)
                        .with("state", encodedState))
                .retrieve()
                .bodyToMono(NaverTokenResponseDto.class)
                .block();
        
        if (naverTokenResponseDto == null) {
            throw new Exception(AuthErrorCode.FAIL_TO_RETRIVE_KAKAO_TOKEN);
        }
        
        if (naverTokenResponseDto.getError() == "invalid_request") {
            throw new Exception(AuthErrorCode.WRONG_PARAMETER_OR_REQUEST);
        }
        // log
        logNaverTokenResponse(naverTokenResponseDto);
        return requestTokenRefresh(naverTokenResponseDto.getRefreshToken());
    }

    private NaverUserInfoResponseDto getUserInfoByToken(String accessToken) {
        WebClient webClient = webClientBuilder.build();
        String response = webClient.get()
                .uri("https://openapi.naver.com/v1/nid/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        if (response == null) {
            throw new Exception(AuthErrorCode.FAIL_TO_RETRIEVE_USER_INFO);
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response, NaverUserInfoResponseDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("사용자 정보 조회 파싱 중 오류 발생", e);
        }
    }

    private String requestTokenRefresh(String refreshToken) {
        WebClient webClient = webClientBuilder.build();
        NaverRequestTokenRefreshDto response = webClient.post()
                .uri("https://nid.naver.com/oauth2.0/token")
                .body(BodyInserters.fromFormData("grant_type", "refresh_token")
                        .with("client_id", naverOAuthRegistrationProperties.getClientId())
                        .with("client_secret", naverOAuthRegistrationProperties.getClientSecret())
                        .with("refresh_token", refreshToken)
                )
                .retrieve()
                .bodyToMono(NaverRequestTokenRefreshDto.class)
                .block();
        if(response == null) {
            throw new Exception(AuthErrorCode.CANNOT_REFRESH_TOKEN);
        }
        // log.info(response.getError());
        // log.info(response.getErrorDescription());
        return response.getAccessToken();

    } 


    private void logNaverTokenResponse(NaverTokenResponseDto naverTokenResponseDto) {
        log.info("Access Token: {}", naverTokenResponseDto.getAccessToken());
        log.info("Refresh Token: {}", naverTokenResponseDto.getRefreshToken());
        log.info("Expires In: {}", naverTokenResponseDto.getExpiresIn());
        log.info("Error: {}", naverTokenResponseDto.getError());
        log.info("Error Description: {}", naverTokenResponseDto.getErrorDescription());
    }

    private String encodeState(String state) {
        try {
            return URLEncoder.encode(state, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Failed to encode state parameter", e);
        }
    }
}
