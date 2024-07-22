package com.sololiving.domain.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.auth.dto.oauth.request.CreateOAuthTokenRequest;
import com.sololiving.domain.auth.dto.oauth.response.OauthUserExistenceResponseDto;
import com.sololiving.domain.auth.service.KakaoOAuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth/kakao")
public class KakaoOAuthController {

    private final KakaoOAuthService kakaoOAuthService;

    @PostMapping("/token")
    public ResponseEntity<OauthUserExistenceResponseDto> postKakaoToken(@RequestBody CreateOAuthTokenRequest createOAuthTokenRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(kakaoOAuthService.checkOauthUserExistence(createOAuthTokenRequest.getAuthCode()));
    }


}
