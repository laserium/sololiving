package com.sololiving.domain.auth.service;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.sololiving.domain.auth.dto.auth.response.SignInResponseDto;
import com.sololiving.domain.auth.dto.oauth.request.CreateOAuthTokenRequest;
import com.sololiving.domain.auth.dto.oauth.response.kakao.KakaoRequestTokenRefreshDto;
import com.sololiving.domain.auth.dto.oauth.response.kakao.KakaoTokenResponseDto;
import com.sololiving.domain.auth.dto.oauth.response.kakao.KakaoUserInfoResponseDto;
import com.sololiving.domain.auth.enums.ClientId;
import com.sololiving.domain.auth.exception.AuthErrorCode;
import com.sololiving.domain.auth.jwt.TokenProvider;
import com.sololiving.domain.user.enums.UserType;
import com.sololiving.domain.user.service.UserAuthService;
import com.sololiving.domain.vo.UserVo;
import com.sololiving.global.config.properties.KakaoOAuthProviderProperties;
import com.sololiving.global.config.properties.KakaoOAuthRegistrationProperties;
import com.sololiving.global.exception.error.ErrorException;
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

    public SignInResponseDto handleKakaoSignInBody(CreateOAuthTokenRequest createOAuthTokenRequest, UserVo userVo, String oauth2UserId) {
        Duration expiresIn = TokenProvider.ACCESS_TOKEN_DURATION;
        UserType userType = UserType.GENERAL;
        ClientId clientId = ClientId.KAKAO;
        if (userVo != null) {
            return createSignInResponseDto(expiresIn, userType, clientId, oauth2UserId);
        } else {
            return createSignInResponseDto(Duration.ZERO, null, clientId, oauth2UserId);
        }
    }

    public UserVo getUserVoFromOAuthToken(CreateOAuthTokenRequest createOAuthTokenRequest) {
        String oauth2UserId = KAKAO_ID_PREFIX + getUserInfoByToken(getTokenByCode(createOAuthTokenRequest.getAuthCode()));
        return userAuthService.findByOauth2UserId(oauth2UserId);
    }

    public String getTokenByCode(String authCode) {
        KakaoTokenResponseDto kakaoTokenResponseDto = fetchKakaoToken(authCode);
        validateTokenResponse(kakaoTokenResponseDto);

        return (kakaoTokenResponseDto.getExpiresIn() <= 10) ? refreshToken(kakaoTokenResponseDto.getRefreshToken()).getAccessToken() : kakaoTokenResponseDto.getAccessToken();
    }

    public String getUserInfoByToken(String accessToken) {
        KakaoUserInfoResponseDto response = fetchKakaoUserInfo(accessToken);
        validateUserInfoResponse(response);
        return response.getId().toString();
    }

    private SignInResponseDto createSignInResponseDto(Duration expiresIn, UserType userType, ClientId clientId, String oauth2UserId) {
        return SignInResponseDto.builder()
                                .expiresIn(expiresIn)
                                .userType(userType)
                                .clientId(clientId)
                                .oauth2UserId(oauth2UserId)
                                .build();
    }

    private void validateTokenResponse(KakaoTokenResponseDto kakaoTokenResponseDto) {
        if (kakaoTokenResponseDto == null) {
            throw new ErrorException(AuthErrorCode.WRONG_PARAMETER_OR_REQUEST);
        }
        oauthUtil.logKakaoTokenResponse(kakaoTokenResponseDto);
    }

    private void validateUserInfoResponse(KakaoUserInfoResponseDto response) {
        if (response == null) {
            throw new ErrorException(AuthErrorCode.FAIL_TO_RETRIEVE_USER_INFO);
        }
    }

    private KakaoTokenResponseDto fetchKakaoToken(String authCode) {
        return webClientBuilder.build()
                               .post()
                               .uri(kakaoOAuthProviderProperties.getTokenUri())
                               .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                                                   .with("client_id", kakaoOAuthRegistrationProperties.getClientId())
                                                   .with("client_secret", kakaoOAuthRegistrationProperties.getClientSecret())
                                                   .with("redirect_uri", kakaoOAuthRegistrationProperties.getRedirectUri())
                                                   .with("code", authCode))
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
