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

    public SignInResponseDto handleNaverSignInBody(UserVo userVo, String oauth2UserId) {
        Duration expiresIn = TokenProvider.ACCESS_TOKEN_DURATION;
        UserType userType = UserType.GENERAL;
        ClientId clientId = ClientId.NAVER;
        if (userVo != null) {
            return createSignInResponseDto(expiresIn, userType, clientId, oauth2UserId);
        } else {
            return createSignInResponseDto(Duration.ZERO, null, clientId, oauth2UserId);
        }
    }

    public UserVo getUserVoFromOAuthToken(CreateOAuthTokenRequestDto requestDto) {
        String oauth2UserId = NAVER_ID_PREFIX
                + getUserInfoByToken(getTokenByCode(requestDto.getAuthCode()));
        return userAuthService.selectByOauth2UserId(oauth2UserId);
    }

    public String getOauth2UserId(CreateOAuthTokenRequestDto requestDto) {
        return NAVER_ID_PREFIX + getUserInfoByToken(getTokenByCode(requestDto.getAuthCode()));
    }

    public String getTokenByCode(String authCode) {
        NaverTokenResponseDto responseDto = fetchNaverToken(authCode);
        validateTokenResponse(responseDto);

        return (responseDto.getExpiresIn() <= 10)
                ? refreshToken(responseDto.getRefreshToken()).getAccessToken()
                : responseDto.getAccessToken();
    }

    public String getUserInfoByToken(String accessToken) {
        NaverUserInfoResponseDto responseDto = fetchNaverUserInfo(accessToken);
        validateUserInfoResponse(responseDto);
        return responseDto.getResponse().getId();
    }

    public void deleteToken(String accessToken) {
        NaverDeleteTokenDto responseDto = deleteNaverToken(accessToken);
        validateDeleteTokenResponse(responseDto);
    }

    private SignInResponseDto createSignInResponseDto(Duration expiresIn, UserType userType, ClientId clientId,
            String oauth2UserId) {
        return SignInResponseDto.builder()
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

    private void validateTokenResponse(NaverTokenResponseDto responseDto) {
        if (responseDto == null || "invalid_request".equals(responseDto.getError())) {
            throw new ErrorException(AuthErrorCode.WRONG_PARAMETER_OR_REQUEST);
        }
        // oauthUtil.logNaverTokenResponse(responseDto);
    }

    private void validateUserInfoResponse(NaverUserInfoResponseDto responseDto) {
        if (responseDto == null) {
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
