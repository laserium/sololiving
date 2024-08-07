package com.sololiving.domain.auth.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.sololiving.domain.auth.dto.auth.response.SignInResponseDto;
import com.sololiving.domain.auth.dto.oauth.request.CreateOAuthTokenRequest;
import com.sololiving.domain.auth.dto.oauth.response.naver.NaverDeleteTokenDto;
import com.sololiving.domain.auth.dto.oauth.response.naver.NaverRefreshTokenDto;
import com.sololiving.domain.auth.dto.oauth.response.naver.NaverTokenResponseDto;
import com.sololiving.domain.auth.dto.oauth.response.naver.NaverUserInfoResponseDto;
import com.sololiving.domain.auth.exception.auth.AuthErrorCode;
import com.sololiving.domain.auth.exception.auth.AuthSuccessCode;
import com.sololiving.domain.user.enums.UserType;
import com.sololiving.domain.user.service.UserAuthService;
import com.sololiving.domain.user.vo.UserVo;
import com.sololiving.global.config.properties.NaverOAuthProviderProperties;
import com.sololiving.global.config.properties.NaverOAuthRegistrationProperties;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.exception.success.SuccessException;
import com.sololiving.global.security.jwt.enums.ClientId;
import com.sololiving.global.security.jwt.service.TokenProvider;
import com.sololiving.global.util.OauthUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NaverOAuthService {

    private final NaverOAuthRegistrationProperties naverOAuthRegistrationProperties;
    private final NaverOAuthProviderProperties naverOAuthProviderProperties;
    private final UserAuthService userAuthService;
    private final WebClient.Builder webClientBuilder;
    private final OauthUtil oauthUtil;

    private static final String NAVER_ID_PREFIX = "NAVER_";

    @Value("${sololiving.naver.oauth2.state}")
    private String state;

    public SignInResponseDto handleNaverSignInBody(CreateOAuthTokenRequest createOAuthTokenRequest, UserVo userVo,
            String oauth2UserId) {
        Duration expiresIn = TokenProvider.ACCESS_TOKEN_DURATION;
        UserType userType = UserType.GENERAL;
        ClientId clientId = ClientId.NAVER;
        if (userVo != null) {
            return createSignInResponseDto(expiresIn, userType, clientId, oauth2UserId);
        } else {
            return createSignInResponseDto(Duration.ZERO, null, clientId, oauth2UserId);
        }
    }

    public UserVo getUserVoFromOAuthToken(CreateOAuthTokenRequest createOAuthTokenRequest) {
        String oauth2UserId = NAVER_ID_PREFIX
                + getUserInfoByToken(getTokenByCode(createOAuthTokenRequest.getAuthCode()));
        return userAuthService.findByOauth2UserId(oauth2UserId);
    }

    public String getOauth2UserId(CreateOAuthTokenRequest createOAuthTokenRequest) {
        return NAVER_ID_PREFIX + getUserInfoByToken(getTokenByCode(createOAuthTokenRequest.getAuthCode()));
    }

    public String getTokenByCode(String authCode) {
        NaverTokenResponseDto naverTokenResponseDto = fetchNaverToken(authCode);
        validateTokenResponse(naverTokenResponseDto);

        return (naverTokenResponseDto.getExpiresIn() <= 10)
                ? refreshToken(naverTokenResponseDto.getRefreshToken()).getAccessToken()
                : naverTokenResponseDto.getAccessToken();
    }

    public String getUserInfoByToken(String accessToken) {
        NaverUserInfoResponseDto response = fetchNaverUserInfo(accessToken);
        validateUserInfoResponse(response);
        return response.getResponse().getId();
    }

    public void deleteToken(String accessToken) {
        NaverDeleteTokenDto response = deleteNaverToken(accessToken);
        validateDeleteTokenResponse(response);
    }

    private SignInResponseDto createSignInResponseDto(Duration expiresIn, UserType userType, ClientId clientId,
            String oauth2UserId) {
        return SignInResponseDto.builder()
                .expiresIn(expiresIn)
                .userType(userType)
                .clientId(clientId)
                .oauth2UserId(oauth2UserId)
                .build();
    }

    private NaverTokenResponseDto fetchNaverToken(String authCode) {
        String encodedState = oauthUtil.encodeState(state);
        return webClientBuilder.build()
                .post()
                .uri(naverOAuthProviderProperties.getTokenUri())
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", naverOAuthRegistrationProperties.getClientId())
                        .with("client_secret", naverOAuthRegistrationProperties.getClientSecret())
                        .with("code", authCode)
                        .with("state", encodedState))
                .retrieve()
                .bodyToMono(NaverTokenResponseDto.class)
                .block();
    }

    private NaverUserInfoResponseDto fetchNaverUserInfo(String accessToken) {
        return webClientBuilder.build()
                .get()
                .uri(naverOAuthProviderProperties.getAuthorizationUri())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(NaverUserInfoResponseDto.class)
                .block();
    }

    private NaverRefreshTokenDto refreshToken(String refreshToken) {
        return webClientBuilder.build()
                .post()
                .uri(naverOAuthProviderProperties.getTokenUri())
                .body(BodyInserters.fromFormData("grant_type", "refresh_token")
                        .with("client_id", naverOAuthRegistrationProperties.getClientId())
                        .with("client_secret", naverOAuthRegistrationProperties.getClientSecret())
                        .with("refresh_token", refreshToken))
                .retrieve()
                .bodyToMono(NaverRefreshTokenDto.class)
                .block();
    }

    private NaverDeleteTokenDto deleteNaverToken(String accessToken) {
        return webClientBuilder.build()
                .post()
                .uri(naverOAuthProviderProperties.getTokenUri())
                .body(BodyInserters.fromFormData("grant_type", "delete")
                        .with("client_id", naverOAuthRegistrationProperties.getClientId())
                        .with("client_secret", naverOAuthRegistrationProperties.getClientSecret())
                        .with("access_token", accessToken)
                        .with("service_provider", "NAVER"))
                .retrieve()
                .bodyToMono(NaverDeleteTokenDto.class)
                .block();
    }

    private void validateTokenResponse(NaverTokenResponseDto naverTokenResponseDto) {
        if (naverTokenResponseDto == null || "invalid_request".equals(naverTokenResponseDto.getError())) {
            throw new ErrorException(AuthErrorCode.WRONG_PARAMETER_OR_REQUEST);
        }
        // oauthUtil.logNaverTokenResponse(naverTokenResponseDto);
    }

    private void validateUserInfoResponse(NaverUserInfoResponseDto response) {
        if (response == null) {
            throw new ErrorException(AuthErrorCode.FAIL_TO_RETRIEVE_USER_INFO);
        }
    }

    private void validateDeleteTokenResponse(NaverDeleteTokenDto response) {
        if ("success".equals(response.getResult())) {
            throw new SuccessException(AuthSuccessCode.SIGN_OUT_SUCCESS);
        } else {
            throw new ErrorException(AuthErrorCode.CANNOT_SIGN_OUT);
        }
    }
}
