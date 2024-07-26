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
import com.sololiving.domain.auth.dto.oauth.response.google.GoogleRefreshTokenDto;
import com.sololiving.domain.auth.dto.oauth.response.google.GoogleTokenResponseDto;
import com.sololiving.domain.auth.dto.oauth.response.google.GoogleUserInfoResponseDto;
import com.sololiving.domain.auth.enums.ClientId;
import com.sololiving.domain.auth.exception.AuthErrorCode;
import com.sololiving.domain.auth.jwt.TokenProvider;
import com.sololiving.domain.user.service.UserService;
import com.sololiving.domain.vo.UserVo;
import com.sololiving.global.common.enums.UserType;
import com.sololiving.global.config.properties.GoogleOAuthProviderProperties;
import com.sololiving.global.config.properties.GoogleOAuthRegistrationProperties;
import com.sololiving.global.exception.error.ErrorException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoogleOAuthService {

    private final GoogleOAuthProviderProperties googleOAuthProviderProperties;
    private final GoogleOAuthRegistrationProperties googleOAuthRegistrationProperties;
    private final UserService userService;
    private final WebClient.Builder webClientBuilder;

    private static final String GOOGLE_ID_PREFIX = "GOOGLE_";

    @Value("${google.oauth2.state}")
    private String state;

    public SignInResponseDto handleGoogleSignInBody(CreateOAuthTokenRequest createOAuthTokenRequest, UserVo userVo, String oauth2UserId) {
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
        return userService.findByOauth2UserId(oauth2UserId);
    }

    public String getOauth2UserId(CreateOAuthTokenRequest createOAuthTokenRequest) {
        return GOOGLE_ID_PREFIX + getUserInfoByToken(getTokenByCode(createOAuthTokenRequest.getAuthCode()));
    }

    public String getUserInfoByToken(String accessToken) {
        GoogleUserInfoResponseDto response = fetchGoogleUserInfo(accessToken);
        validateUserInfoResponse(response);
        return response.getId();
    }

    public String getTokenByCode(String authCode) {
        GoogleTokenResponseDto googleTokenResponseDto = fetchGoogleToken(authCode);
        validateTokenResponse(googleTokenResponseDto);

        return (googleTokenResponseDto.getExpiresIn() <= 10) ? refreshToken(googleTokenResponseDto.getRefreshToken()).getAccessToken() : googleTokenResponseDto.getAccessToken();
    }

    private SignInResponseDto createSignInResponseDto(Duration expiresIn, UserType userType, ClientId clientId, String oauth2UserId) {
        return SignInResponseDto.builder()
                                .expiresIn(expiresIn)
                                .userType(userType)
                                .clientId(clientId)
                                .oauth2UserId(oauth2UserId)
                                .build();
    }

    private void validateTokenResponse(GoogleTokenResponseDto googleTokenResponseDto) {
        if (googleTokenResponseDto == null) {
            throw new ErrorException(AuthErrorCode.WRONG_PARAMETER_OR_REQUEST);
        }
        // oauthUtil.logGoogleTokenResponse(googleTokenResponseDto);
    }

    private void validateUserInfoResponse(GoogleUserInfoResponseDto response) {
        if (response == null) {
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
