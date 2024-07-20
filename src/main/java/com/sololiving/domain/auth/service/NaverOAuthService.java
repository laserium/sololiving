package com.sololiving.domain.auth.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sololiving.domain.auth.dto.auth.request.SignInRequestDto;
import com.sololiving.domain.auth.dto.auth.request.SignUpRequestDto;
import com.sololiving.domain.auth.dto.oauth.response.NaverTokenResponseDto;
import com.sololiving.domain.auth.dto.oauth.response.NaverUserInfoResponseDto;
import com.sololiving.domain.auth.dto.token.response.CreateTokenResponse;
import com.sololiving.domain.auth.exception.AuthErrorCode;
import com.sololiving.domain.user.mapper.UserMapper;
import com.sololiving.domain.vo.UserVo;
import com.sololiving.global.common.enums.UserType;
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

    @Transactional
    public CreateTokenResponse naverSignIn(String authCode) {
        NaverUserInfoResponseDto userInfo = getUserInfo(authCode);
        String naverUserId = NAVER_ID_PREFIX + userInfo.getId();

        return userMapper.findByUserId(naverUserId)
                .map(this::handleExistingUser)
                .orElseGet(() -> handleNewUser(userInfo));
    }

    public SignInRequestDto naverSignInRequest(String authCode) {
        NaverUserInfoResponseDto userInfo = getUserInfo(authCode);
        return SignInRequestDto.builder()
                .userId(NAVER_ID_PREFIX + userInfo.getId())
                .userPwd(state)
                .build();
    }

    private NaverUserInfoResponseDto getUserInfo(String authCode) {
        String accessToken = postNaverToken(authCode);
        return getUserInfoByNaverAccessToken(accessToken);
    }

    private CreateTokenResponse handleExistingUser(UserVo user) {
        SignInRequestDto signInRequest = SignInRequestDto.builder()
                .userId(user.getUserId())
                .userPwd(null)
                .build();
        return authService.signIn(signInRequest);
    }

    private CreateTokenResponse handleNewUser(NaverUserInfoResponseDto userInfo) {
        SignUpRequestDto signUpRequestDto = createSignUpRequest(userInfo);
        userMapper.saveUser(signUpRequestDto);
        SignInRequestDto signInRequestDto = SignInRequestDto.builder()
                .userId(signUpRequestDto.getUserId())
                .userPwd(null)
                .build();
        return authService.signIn(signInRequestDto);
    }

    private String postNaverToken(String authCode) {
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

        if ("invalid_request".equals(naverTokenResponseDto.getError()) && "no valid data in session".equals(naverTokenResponseDto.getErrorDescription())) {
            throw new Exception(AuthErrorCode.NO_VALID_DATA_IN_SESSION);
        }

        logNaverTokenResponse(naverTokenResponseDto);
        return naverTokenResponseDto.getAccessToken();
    }

    private NaverUserInfoResponseDto getUserInfoByNaverAccessToken(String accessToken) {
        WebClient webClient = webClientBuilder.build();
        String response = webClient.get()
                .uri("https://openapi.naver.com/v1/nid/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        log.info("Naver User Info Response: {}", response);

        if (response == null) {
            throw new Exception(AuthErrorCode.FAIL_TO_RETRIEVE_USER_INFO);
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response, NaverUserInfoResponseDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse Naver user info response", e);
        }
    }

    private SignUpRequestDto createSignUpRequest(NaverUserInfoResponseDto userInfo) {
        log.info(userInfo.getMobile());
        String sanitizedContact = userInfo.getMobile().replaceAll("-", "");
        log.info(sanitizedContact);
        return SignUpRequestDto.builder()
                .userId(NAVER_ID_PREFIX + userInfo.getId())
                .userPwd(null)
                .contact(sanitizedContact)
                .email(userInfo.getEmail())
                .userType(UserType.GENERAL)
                .build();
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
