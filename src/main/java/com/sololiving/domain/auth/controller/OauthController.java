package com.sololiving.domain.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.auth.dto.auth.response.SignInResponseDto;
import com.sololiving.domain.auth.dto.oauth.request.CreateOAuthTokenRequest;
import com.sololiving.domain.auth.dto.oauth.response.OauthUserExistenceResponseDto;
import com.sololiving.domain.auth.exception.auth.AuthSuccessCode;
import com.sololiving.domain.auth.service.AuthService;
import com.sololiving.domain.auth.service.GoogleOAuthService;
import com.sololiving.domain.auth.service.KakaoOAuthService;
import com.sololiving.domain.auth.service.NaverOAuthService;
import com.sololiving.domain.auth.service.OAuthService;
import com.sololiving.domain.user.vo.UserVo;
import com.sololiving.global.exception.ResponseMessage;
import com.sololiving.global.exception.success.SuccessResponse;
import com.sololiving.global.security.jwt.dto.response.AuthTokenResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class OauthController {

    private final NaverOAuthService naverOAuthService;
    private final KakaoOAuthService kakaoOAuthService;
    private final GoogleOAuthService googleOAuthService;
    private final OAuthService oAuthService;
    private final AuthService authService;

    @PostMapping("/naver/token")
    public ResponseEntity<?> postNaverToken(@RequestBody CreateOAuthTokenRequest createOAuthTokenRequest) {
        String oauth2UserId = naverOAuthService.getOauth2UserId(createOAuthTokenRequest);
        UserVo userVo = naverOAuthService.getUserVoFromOAuthToken(createOAuthTokenRequest);
        // 로그인 처리
        if (userVo != null) {
            SignInResponseDto responseBody = naverOAuthService.handleNaverSignInBody(createOAuthTokenRequest, userVo,
                    userVo.getOauth2UserId());
            AuthTokenResponseDto responseHeader = oAuthService.handleSignInHeader(userVo, responseBody.getClientId());
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Set-Cookie",
                            authService.createAccessTokenCookie(responseHeader.getAccessToken()).toString())
                    .header("Set-Cookie",
                            authService.createRefreshTokenCookie(responseHeader.getRefreshToken()).toString())
                    .body(responseBody);
            // 회원가입 처리
        } else {
            OauthUserExistenceResponseDto oauthUserExistenceResponseDto = OauthUserExistenceResponseDto.builder()
                    .isOauthUser("true")
                    .oauth2UserId(oauth2UserId)
                    .build();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(oauthUserExistenceResponseDto);
        }
    }

    @PostMapping("/kakao/token")
    public ResponseEntity<?> postKakaoToken(@RequestBody CreateOAuthTokenRequest createOAuthTokenRequest) {
        String oauth2UserId = kakaoOAuthService.getOauth2UserId(createOAuthTokenRequest);
        UserVo userVo = kakaoOAuthService.getUserVoFromOAuthToken(createOAuthTokenRequest);
        // 로그인 처리
        if (userVo != null) {
            SignInResponseDto responseBody = kakaoOAuthService.handleKakaoSignInBody(createOAuthTokenRequest, userVo,
                    userVo.getOauth2UserId());
            AuthTokenResponseDto responseHeader = oAuthService.handleSignInHeader(userVo, responseBody.getClientId());
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Set-Cookie",
                            authService.createAccessTokenCookie(responseHeader.getAccessToken()).toString())
                    .header("Set-Cookie",
                            authService.createRefreshTokenCookie(responseHeader.getRefreshToken()).toString())
                    .body(responseBody);
            // 회원가입 처리
        } else {
            OauthUserExistenceResponseDto oauthUserExistenceResponseDto = OauthUserExistenceResponseDto.builder()
                    .isOauthUser("true")
                    .oauth2UserId(oauth2UserId)
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(oauthUserExistenceResponseDto);
        }
    }

    @PostMapping("/google/token")
    public ResponseEntity<?> postGoogleToken(@RequestBody CreateOAuthTokenRequest createOAuthTokenRequest) {
        String oauth2UserId = googleOAuthService.getOauth2UserId(createOAuthTokenRequest);
        UserVo userVo = googleOAuthService.getUserVoFromOAuthToken(oauth2UserId);
        // 로그인 처리
        if (userVo != null) {
            SignInResponseDto responseBody = googleOAuthService.handleGoogleSignInBody(createOAuthTokenRequest, userVo,
                    userVo.getOauth2UserId());
            AuthTokenResponseDto responseHeader = oAuthService.handleSignInHeader(userVo, responseBody.getClientId());
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Set-Cookie",
                            authService.createAccessTokenCookie(responseHeader.getAccessToken()).toString())
                    .header("Set-Cookie",
                            authService.createRefreshTokenCookie(responseHeader.getRefreshToken()).toString())
                    .body(responseBody);
            // 회원가입 처리
        } else {
            OauthUserExistenceResponseDto oauthUserExistenceResponseDto = OauthUserExistenceResponseDto.builder()
                    .isOauthUser("true")
                    .oauth2UserId(oauth2UserId)
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(oauthUserExistenceResponseDto);
        }
    }
}
