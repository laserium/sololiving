package com.sololiving.domain.auth.service;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.sololiving.domain.auth.dto.auth.response.SignInResponseDto;
import com.sololiving.domain.auth.dto.oauth.request.CreateOAuthTokenRequestDto;
import com.sololiving.domain.auth.dto.oauth.response.kakao.KakaoRequestTokenRefreshDto;
import com.sololiving.domain.auth.dto.oauth.response.kakao.KakaoTokenResponseDto;
import com.sololiving.domain.auth.dto.oauth.response.kakao.KakaoUserInfoResponseDto;
import com.sololiving.domain.auth.exception.auth.AuthErrorCode;
import com.sololiving.domain.user.enums.UserType;
import com.sololiving.domain.user.service.UserAuthService;
import com.sololiving.domain.user.vo.UserVo;
import com.sololiving.global.config.properties.KakaoOAuthProviderProperties;
import com.sololiving.global.config.properties.KakaoOAuthRegistrationProperties;
import com.sololiving.global.exception.GlobalErrorCode;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.security.jwt.enums.ClientId;
import com.sololiving.global.security.jwt.service.TokenProvider;
import com.sololiving.global.util.OauthUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KakaoOAuthService {

    private final KakaoOAuthRegistrationProperties kakaoOAuthRegistrationProperties;
    private final KakaoOAuthProviderProperties kakaoOAuthProviderProperties;
    private final UserAuthService userAuthService;
    private final WebClient.Builder webClientBuilder;
    private final OauthUtil oauthUtil;

    private static final String KAKAO_ID_PREFIX = "KAKAO_";

    public SignInResponseDto handleKakaoSignInBody(UserVo userVo, String oauth2UserId) {
        Duration expiresIn = TokenProvider.ACCESS_TOKEN_DURATION;
        UserType userType = UserType.GENERAL;
        ClientId clientId = ClientId.KAKAO;
        if (userVo != null) {
            return createSignInResponseDto(expiresIn, userType, clientId, oauth2UserId);
        } else {
            return createSignInResponseDto(Duration.ZERO, null, clientId, oauth2UserId);
        }
    }

    public UserVo getUserVoFromOAuthToken(CreateOAuthTokenRequestDto requestDto) {
        String oauth2UserId = KAKAO_ID_PREFIX
                + getUserInfoByToken(getTokenByCode(requestDto.getAuthCode()));
        return userAuthService.selectByOauth2UserId(oauth2UserId);
    }

    public String getOauth2UserId(CreateOAuthTokenRequestDto requestDto) {
        return KAKAO_ID_PREFIX + getUserInfoByToken(getTokenByCode(requestDto.getAuthCode()));
    }

    public String getTokenByCode(String authCode) {
        KakaoTokenResponseDto responseDto = fetchKakaoToken(authCode);
        validateTokenResponse(responseDto);

        return (responseDto.getExpiresIn() <= 10)
                ? refreshToken(responseDto.getRefreshToken()).getAccessToken()
                : responseDto.getAccessToken();
    }

    public String getUserInfoByToken(String accessToken) {
        KakaoUserInfoResponseDto response = fetchKakaoUserInfo(accessToken);
        validateUserInfoResponse(response);
        return response.getId().toString();
    }

    private SignInResponseDto createSignInResponseDto(Duration expiresIn, UserType userType, ClientId clientId,
            String oauth2UserId) {
        return SignInResponseDto.builder()
                .userType(userType)
                .clientId(clientId)
                .oauth2UserId(oauth2UserId)
                .build();
    }

    private void validateTokenResponse(KakaoTokenResponseDto responseDto) {
        if (responseDto == null) {
            throw new ErrorException(GlobalErrorCode.REQUEST_IS_NULL);
        }
        oauthUtil.logKakaoTokenResponse(responseDto);
    }

    private void validateUserInfoResponse(KakaoUserInfoResponseDto responseDto) {
        if (responseDto == null) {
            throw new ErrorException(AuthErrorCode.FAIL_TO_RETRIEVE_USER_INFO);
        }
    }

    private KakaoTokenResponseDto fetchKakaoToken(String authCode) {
        return webClientBuilder.build()
                .post()
                .uri(kakaoOAuthProviderProperties.getTokenUri())
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8")
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", kakaoOAuthRegistrationProperties.getClientId())
                        .with("redirect_uri", kakaoOAuthRegistrationProperties.getRedirectUri())
                        .with("code", authCode)
                        .with("client_secret", kakaoOAuthRegistrationProperties.getClientSecret()))
                .retrieve()
                .bodyToMono(KakaoTokenResponseDto.class)
                .block();
    }

    private KakaoUserInfoResponseDto fetchKakaoUserInfo(String accessToken) {
        return webClientBuilder.build()
                .get()
                .uri(kakaoOAuthProviderProperties.getUserInfoUri())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(KakaoUserInfoResponseDto.class)
                .block();
    }

    private KakaoRequestTokenRefreshDto refreshToken(String refreshToken) {
        return webClientBuilder.build()
                .post()
                .uri(kakaoOAuthProviderProperties.getTokenUri())
                .body(BodyInserters.fromFormData("grant_type", "refresh_token")
                        .with("client_id", kakaoOAuthRegistrationProperties.getClientId())
                        .with("client_secret", kakaoOAuthRegistrationProperties.getClientSecret())
                        .with("refresh_token", refreshToken))
                .retrieve()
                .bodyToMono(KakaoRequestTokenRefreshDto.class)
                .block();
    }
}
