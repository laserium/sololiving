package com.sololiving.domain.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.sololiving.domain.auth.dto.KakaoDto.KakaoTokenValidationResponseDto;
import com.sololiving.domain.auth.service.KakaoOauthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class OauthController {

    private final KakaoOauthService KakaoOauthService;

    @PostMapping("/kakao/token/validation")
    public ResponseEntity<KakaoTokenValidationResponseDto> postKakaoTokenValidation() {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
    
    

    
}
