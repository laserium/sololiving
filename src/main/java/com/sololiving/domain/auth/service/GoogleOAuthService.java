package com.sololiving.domain.auth.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.sololiving.domain.auth.dto.auth.response.SignInResponseDto;
import com.sololiving.domain.auth.dto.oauth.request.CreateOAuthTokenRequestDto;
import com.sololiving.domain.auth.dto.oauth.response.google.GoogleRefreshTokenDto;
import com.sololiving.domain.auth.dto.oauth.response.google.GoogleTokenResponseDto;
import com.sololiving.domain.auth.dto.oauth.response.google.GoogleUserInfoResponseDto;
import com.sololiving.domain.auth.exception.auth.AuthErrorCode;
import com.sololiving.domain.user.enums.UserType;
import com.sololiving.domain.user.service.UserAuthService;
import com.sololiving.domain.user.vo.UserVo;
import com.sololiving.global.config.properties.GoogleOAuthProviderProperties;
import com.sololiving.global.config.properties.GoogleOAuthRegistrationProperties;
import com.sololiving.global.exception.GlobalErrorCode;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.security.jwt.enums.ClientId;
import com.sololiving.global.security.jwt.service.TokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoogleOAuthService {

    private final GoogleOAuthProviderProperties googleOAuthProviderProperties;
    private final GoogleOAuthRegistrationProperties googleOAuthRegistrationProperties;
    private final UserAuthService userAuthService;
    private final WebClient.Builder webClientBuilder;

    private static final String GOOGLE_ID_PREFIX = "GOOGLE_";

    @Value("${sololiving.google.oauth2.state}")
    private String state;

    public SignInResponseDto handleGoogleSignInBody(UserVo userVo,
            String oauth2UserId) {
        Duration expiresIn = TokenProvider.ACCESS_TOKEN_DURATION;
        UserType userType = UserType.GENERAL;
        ClientId clientId = ClientId.GOOGLE;
        if (userVo != null) {
            return createSignInResponseDto(expiresIn, userType, clientId, oauth2UserId);
        } else {
            return createSignInResponseDto(Duration.ZERO, null, clientId, oauth2UserId);
        }
    }

    public UserVo getUserVoFromOAuthToken(String oauth2UserId) {
        return userAuthService.selectByOauth2UserId(oauth2UserId);
    }

    public String getOauth2UserId(CreateOAuthTokenRequestDto requestDto) {
        return GOOGLE_ID_PREFIX + getUserInfoByToken(getTokenByCode(requestDto.getAuthCode()));
    }

    public String getUserInfoByToken(String accessToken) {
        GoogleUserInfoResponseDto responseDto = fetchGoogleUserInfo(accessToken);
        validateUserInfoResponse(responseDto);
        return responseDto.getId();
    }

    public String getTokenByCode(String authCode) {
        GoogleTokenResponseDto responseDto = fetchGoogleToken(authCode);
        validateTokenResponse(responseDto);

        return (responseDto.getExpiresIn() <= 10)
                ? refreshToken(responseDto.getRefreshToken()).getAccessToken()
                : responseDto.getAccessToken();
    }

    private SignInResponseDto createSignInResponseDto(Duration expiresIn, UserType userType, ClientId clientId,
            String oauth2UserId) {
        return SignInResponseDto.builder()
                .userType(userType)
                .clientId(clientId)
                .oauth2UserId(oauth2UserId)
                .build();
    }

    private void validateTokenResponse(GoogleTokenResponseDto responseDto) {
        if (responseDto == null) {
            throw new ErrorException(GlobalErrorCode.REQUEST_IS_NULL);
        }
        // oauthUtil.logGoogleTokenResponse(googleTokenResponseDto);
    }

    private void validateUserInfoResponse(GoogleUserInfoResponseDto responseDto) {
        if (responseDto == null) {
            throw new ErrorException(AuthErrorCode.FAIL_TO_RETRIEVE_USER_INFO);
        }
    }

    private GoogleTokenResponseDto fetchGoogleToken(String authCode) {
        return webClientBuilder.build()
                .post()
                .uri(googleOAuthProviderProperties.getTokenUri())
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", googleOAuthRegistrationProperties.getClientId())
                        .with("client_secret", googleOAuthRegistrationProperties.getClientSecret())
                        .with("redirect_uri", googleOAuthRegistrationProperties.getRedirectUri())
                        .with("code", authCode)
                        .with("state", state))
                .retrieve()
                .bodyToMono(GoogleTokenResponseDto.class)
                .block();
    }

    private GoogleUserInfoResponseDto fetchGoogleUserInfo(String accessToken) {
        return webClientBuilder.build()
                .get()
                .uri(googleOAuthProviderProperties.getAuthorizationUri())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(GoogleUserInfoResponseDto.class)
                .block();
    }

    private GoogleRefreshTokenDto refreshToken(String refreshToken) {
        return webClientBuilder.build()
                .post()
                .uri(googleOAuthProviderProperties.getTokenUri())
                .body(BodyInserters.fromFormData("grant_type", "refresh_token")
                        .with("client_id", googleOAuthRegistrationProperties.getClientId())
                        .with("client_secret", googleOAuthRegistrationProperties.getClientSecret())
                        .with("refresh_token", refreshToken))
                .retrieve()
                .bodyToMono(GoogleRefreshTokenDto.class)
                .block();
    }
}
