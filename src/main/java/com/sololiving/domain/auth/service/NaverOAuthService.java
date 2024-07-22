package com.sololiving.domain.auth.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sololiving.domain.auth.dto.auth.request.SignInRequestDto;
import com.sololiving.domain.auth.dto.oauth.response.OauthUserExistenceResponseDto;
import com.sololiving.domain.auth.dto.oauth.response.kakao.KakaoUserInfoResponseDto;
import com.sololiving.domain.auth.dto.oauth.response.naver.NaverDeleteTokenDto;
import com.sololiving.domain.auth.dto.oauth.response.naver.NaverRefreshTokenhDto;
import com.sololiving.domain.auth.dto.oauth.response.naver.NaverTokenResponseDto;
import com.sololiving.domain.auth.dto.oauth.response.naver.NaverUserInfoResponseDto;
import com.sololiving.domain.auth.enums.ClientId;
import com.sololiving.domain.auth.exception.AuthErrorCode;
import com.sololiving.domain.auth.exception.AuthSuccessCode;
import com.sololiving.domain.user.mapper.UserMapper;
import com.sololiving.domain.vo.UserVo;
import com.sololiving.global.config.properties.NaverOAuthProviderProperties;
import com.sololiving.global.config.properties.NaverOAuthRegistrationProperties;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.exception.success.SuccessException;
import com.sololiving.global.util.OauthUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverOAuthService {

    private final NaverOAuthRegistrationProperties naverOAuthRegistrationProperties;
    private final NaverOAuthProviderProperties naverOAuthProviderProperties;
    private final UserMapper userMapper;
    private final AuthService authService;
    private final WebClient.Builder webClientBuilder;
    private final OauthUtil oauthUtil;

    private static final String NAVER_ID_PREFIX = "NAVER_";

    @Value("${naver.oauth2.state}")
    private String state;

    public OauthUserExistenceResponseDto checkOauthUserExistence(String authCode) {
        String oauth2UserId = NAVER_ID_PREFIX + getUserInfoByToken(getTokenByCode(authCode));
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
        String encodedState = oauthUtil.encodeState(state);
        WebClient webClient = webClientBuilder.build();
        NaverTokenResponseDto naverTokenResponseDto = webClient.post()
                .uri(naverOAuthProviderProperties.getTokenUri())
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", naverOAuthRegistrationProperties.getClientId())
                        .with("client_secret", naverOAuthRegistrationProperties.getClientSecret())
                        .with("code", authCode)
                        .with("state", encodedState))
                .retrieve()
                .bodyToMono(NaverTokenResponseDto.class)
                .block();
        
        if (naverTokenResponseDto == null) {
            throw new ErrorException(AuthErrorCode.FAIL_TO_RETRIVE_KAKAO_TOKEN);
        }
        
        if (naverTokenResponseDto.getError() == "invalid_request") {
            throw new ErrorException(AuthErrorCode.WRONG_PARAMETER_OR_REQUEST);
        }
        // log
        oauthUtil.logNaverTokenResponse(naverTokenResponseDto);

        if(naverTokenResponseDto.getExpiresIn() >= 10) {
            return refreshToken(naverTokenResponseDto.getRefreshToken());
        } else return naverTokenResponseDto.getAccessToken();

    }

    private String getUserInfoByToken(String accessToken) {
        WebClient webClient = webClientBuilder.build();
        log.info(accessToken);
        NaverUserInfoResponseDto response = webClient.get()
                .uri(naverOAuthProviderProperties.getAuthorizationUri())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + OauthUtil.encodeState(accessToken))
                .retrieve()
                .bodyToMono(NaverUserInfoResponseDto.class)
                .block();

        if (response == null) {
            throw new ErrorException(AuthErrorCode.FAIL_TO_RETRIEVE_USER_INFO);
        }
        try {
            // KakaoUserInfoResponseDto response = objectMapper.readValue(responseBody, KakaoUserInfoResponseDto.class);
            // log.info(response.getId().toString());
        } catch (Exception e) {
            
        }
        return response.getResponse().getId();

    }

    private String refreshToken(String refreshToken) {
        WebClient webClient = webClientBuilder.build();
        NaverRefreshTokenhDto response = webClient.post()
                .uri(naverOAuthProviderProperties.getTokenUri())
                .body(BodyInserters.fromFormData("grant_type", "refresh_token")
                        .with("client_id", naverOAuthRegistrationProperties.getClientId())
                        .with("client_secret", naverOAuthRegistrationProperties.getClientSecret())
                        .with("refresh_token", refreshToken)
                )
                .retrieve()
                .bodyToMono(NaverRefreshTokenhDto.class)
                .block();
        if(response == null) {
            throw new ErrorException(AuthErrorCode.CANNOT_REFRESH_TOKEN);
        }
        // log.info(response.getError());
        // log.info(response.getErrorDescription());
        return response.getAccessToken();
    
    } 


    public void deleteToken(String accessToken) {
        WebClient webClient = webClientBuilder.build();
        NaverDeleteTokenDto response = webClient.post()
        .uri(naverOAuthProviderProperties.getTokenUri())
        .body(BodyInserters.fromFormData("grant_type", "delete")
                .with("client_id", naverOAuthRegistrationProperties.getClientId())
                .with("client_secret", naverOAuthRegistrationProperties.getClientSecret())
                .with("access_token", accessToken)
                .with("service_provider", "NAVER")
        )
        .retrieve()
        .bodyToMono(NaverDeleteTokenDto.class)
        .block();

        if(response.getResult() == "success") { 
            throw new SuccessException(AuthSuccessCode.SIGN_OUT_SUCCESS);
        } else {
            throw new ErrorException(AuthErrorCode.CANNOT_SIGN_OUT);
        }
    }
    

}
