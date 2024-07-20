package com.sololiving.domain.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.auth.dto.oauth.response.KakaoTokenValidationResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth/kakao")
public class KakaoOAuthController {
    
    // @PostMapping("/kakao/token")
    // public ResponseEntity<?> getKakaoToken() {
        // log.info(authCode);
    //     String authCode = "fPktpTazSb3dBr2Tfb0n235CMTKhPrzx0jietlbG6oqbbkpS_2blFwAAAAQKKiWOAAABkMRYCIHo6jj-qNQmaA";
    //     KakaoOAuthService.getKakaoToken(authCode);
    //     return ResponseEntity.status(HttpStatus.CREATED).body(null);
    // }
    
    @PostMapping("/token/validation")
    public ResponseEntity<KakaoTokenValidationResponseDto> postKakaoTokenValidation() {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
