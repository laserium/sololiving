package com.sololiving.domain.auth.service;

import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.sololiving.domain.auth.dto.auth.request.SignInRequestDto;
import com.sololiving.domain.auth.dto.oauth.response.OauthUserExistenceResponseDto;
import com.sololiving.domain.auth.dto.oauth.response.kakao.KakaoRequestTokenRefreshDto;
import com.sololiving.domain.auth.dto.oauth.response.kakao.KakaoTokenResponseDto;
import com.sololiving.domain.auth.dto.oauth.response.kakao.KakaoUserInfoResponseDto;
import com.sololiving.domain.auth.enums.ClientId;
import com.sololiving.domain.auth.exception.AuthErrorCode;
import com.sololiving.domain.user.mapper.UserMapper;
import com.sololiving.domain.vo.UserVo;
import com.sololiving.global.config.properties.KakaoOAuthProviderProperties;
import com.sololiving.global.config.properties.KakaoOAuthRegistrationProperties;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.util.OauthUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class KakaoOAuthService {

    private final KakaoOAuthRegistrationProperties kakaoOAuthRegistrationProperties;
    private final KakaoOAuthProviderProperties kakaoOAuthProviderProperties;
    private final UserMapper userMapper;
    private final AuthService authService;
    private final WebClient.Builder webClientBuilder;
    private final OauthUtil oauthUtil;

    private static final String KAKAO_ID_PREFIX = "KAKAO_";

    public OauthUserExistenceResponseDto checkOauthUserExistence(String authCode) {
        String oauth2UserId = KAKAO_ID_PREFIX + getUserInfoByToken(getTokenByCode(authCode));
        Optional<UserVo> userOptional = userMapper.findByOauth2UserId(oauth2UserId);
        if (userOptional.isPresent()) {
            SignInRequestDto signInRequestDto = SignInRequestDto.builder()
                                                .userId(userOptional.get().getUserId())
                                                .userPwd(null)
                                                .clientId(ClientId.KAKAO)
                                                .build();
            authService.signIn(signInRequestDto);
            return OauthUserExistenceResponseDto.builder().isOauthUser("true").oauth2UserId(oauth2UserId).build();
        } else {
            return OauthUserExistenceResponseDto.builder().isOauthUser("false").oauth2UserId(oauth2UserId).build();
        }
    }

    public String getTokenByCode(String authCode) {
        WebClient webClient = webClientBuilder.build();
        try {
            KakaoTokenResponseDto response = webClient.post()
                    .uri(kakaoOAuthProviderProperties.getTokenUri())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                            .with("client_id", kakaoOAuthRegistrationProperties.getClientId())
                            .with("client_secret", kakaoOAuthRegistrationProperties.getClientSecret())
                            .with("redirect_uri", kakaoOAuthRegistrationProperties.getRedirectUri())
                            .with("code", authCode)
                            // .with("client_secret", kakaoOAuthRegistrationProperties.getClientSecret())
                    )
                    .retrieve()
                    .bodyToMono(KakaoTokenResponseDto.class)
                    .block();

            if (response != null) {
                oauthUtil.logKakaoTokenResponse(response);
                if(response.getExpiresIn() <= 10) {
                    return requestTokenRefresh(response.getRefreshToken());
                } else {
                    return response.getAccessToken();
                }
            } else {
                throw new ErrorException(AuthErrorCode.FAIL_TO_RETRIVE_KAKAO_TOKEN);
            }
        } catch (WebClientResponseException e) {
            log.error("Error response from Kakao token endpoint: {}", e.getResponseBodyAsString());
            throw new ErrorException(AuthErrorCode.FAIL_TO_RETRIVE_KAKAO_TOKEN);
        }
    }

    private String getUserInfoByToken(String accessToken) {
        WebClient webClient = webClientBuilder.build();
        KakaoUserInfoResponseDto response = webClient.post()
                .uri(kakaoOAuthProviderProperties.getUserInfoUri())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .retrieve()
                .bodyToMono(KakaoUserInfoResponseDto.class)
                .block();

        if (response == null) {
            throw new ErrorException(AuthErrorCode.FAIL_TO_RETRIEVE_USER_INFO);
        }

        return response.getId().toString();
    }

    private String requestTokenRefresh(String refreshToken) {
        WebClient webClient = webClientBuilder.build();
        KakaoRequestTokenRefreshDto response = webClient.post()
                    .uri(kakaoOAuthProviderProperties.getTokenUri())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .body(BodyInserters.fromFormData("grant_type", "refresh_token")
                        .with("client_id", kakaoOAuthRegistrationProperties.getClientId())
                        .with("refresh_token", refreshToken)
                        .with("client_secret", kakaoOAuthRegistrationProperties.getClientSecret())
                )
                .retrieve()
                .bodyToMono(KakaoRequestTokenRefreshDto.class)
                .block();
            
        if(response == null) {
            throw new ErrorException(AuthErrorCode.CANNOT_REFRESH_TOKEN);
        }
        // log.info(response.getError());
        // log.info(response.getErrorDescription());
        return response.getAccessToken();
    }
}
