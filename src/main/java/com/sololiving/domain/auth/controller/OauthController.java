package com.sololiving.domain.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.auth.service.OauthService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class OauthController {

    private final OauthService oAuthService;

    @GetMapping("/kakao/token")
    public ResponseEntity<?> getKakaoToken(@RequestParam("code") String authcode) {
        String accessToken = oAuthService.getKakaoToken(authcode);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
    
    

    
}
