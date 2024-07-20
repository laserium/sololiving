package com.sololiving.domain.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.auth.dto.auth.request.SignInRequestDto;
import com.sololiving.domain.auth.dto.auth.response.SignInResponseDto;
import com.sololiving.domain.auth.dto.oauth.request.CreateOAuthTokenRequest;
import com.sololiving.domain.auth.dto.token.response.CreateTokenResponse;
import com.sololiving.domain.auth.service.AuthService;
import com.sololiving.domain.auth.service.NaverOAuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth/naver")
public class NaverOAuthController {

    private final NaverOAuthService naverOAuthService;
    private final AuthService authService;
    
    // 네이버 토큰 요청 + 자동 회원가입 + 자동 로그인
    @PostMapping("/token")
    public ResponseEntity<SignInResponseDto> postNaverToken(@RequestBody CreateOAuthTokenRequest createOAuthTokenRequest) {
        CreateTokenResponse tokenResponse = naverOAuthService.naverSignIn(createOAuthTokenRequest.getAuthCode());
        ResponseCookie refreshTokenCookie = authService.createRefreshTokenCookie(tokenResponse.getRefreshToken());
        SignInRequestDto signInRequestDto = naverOAuthService.naverSignInRequest(createOAuthTokenRequest.getAuthCode());
        SignInResponseDto signInResponseDto;
        try {
            signInResponseDto = authService.createSignInResponse(signInRequestDto, tokenResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Set-Cookie", refreshTokenCookie.toString())
                .body(signInResponseDto);
    }

}
