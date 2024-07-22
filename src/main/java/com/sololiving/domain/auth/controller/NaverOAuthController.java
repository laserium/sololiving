package com.sololiving.domain.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.auth.dto.oauth.request.CreateOAuthTokenRequest;
import com.sololiving.domain.auth.dto.oauth.response.OauthUserExistenceResponseDto;
import com.sololiving.domain.auth.service.NaverOAuthService;
import com.sololiving.global.util.CookieService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth/naver")
public class NaverOAuthController {

    private final NaverOAuthService naverOAuthService;
    
    // 네이버 토큰 요청 + 자동 회원가입 or 자동 로그인 핸들링
    @PostMapping("/token")
    public ResponseEntity<OauthUserExistenceResponseDto> postNaverToken(@RequestBody CreateOAuthTokenRequest createOAuthTokenRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(naverOAuthService.checkOauthUserExistence(createOAuthTokenRequest.getAuthCode()));
    }

    @DeleteMapping("/token")
    public ResponseEntity<?> postMethodName(HttpServletRequest httpServletRequest) {
        String accessToken = CookieService.extractAccessTokenFromCookie(httpServletRequest);
        naverOAuthService.deleteToken(accessToken);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
    
}
