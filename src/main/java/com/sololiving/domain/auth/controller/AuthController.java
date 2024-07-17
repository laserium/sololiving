package com.sololiving.domain.auth.controller;

import java.time.Duration;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.auth.dto.AuthDto.SignInRequest;
import com.sololiving.domain.auth.dto.AuthDto.SignInResponse;
import com.sololiving.domain.auth.dto.AuthDto.SignUpRequest;
import com.sololiving.domain.auth.dto.TokenDto.CreateTokensResponse;
import com.sololiving.domain.auth.enums.ClientId;
import com.sololiving.domain.auth.exception.AuthErrorCode;
import com.sololiving.domain.auth.mapper.RefreshTokenMapper;
import com.sololiving.domain.auth.service.AuthService;
import com.sololiving.domain.user.service.UserService;
import com.sololiving.domain.vo.RefreshTokenVo;
import com.sololiving.domain.vo.UserVo;
import com.sololiving.global.common.enums.UserType;
import com.sololiving.global.exception.Exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/auth")
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping("/signup")
    public ResponseEntity<?> postSignUp(@RequestBody SignUpRequest signUpRequest) {
        authService.signUp(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("SUCCESS TO SIGNUP");
    }

    @PostMapping("/signin")
    public ResponseEntity<SignInResponse> postSignIn(@RequestBody SignInRequest signInRequest) {
        CreateTokensResponse tokensResponse = authService.signIn(signInRequest);
        ResponseCookie refreshTokenCookie = authService.createRefreshTokenCookie(tokensResponse.getRefreshToken());
        SignInResponse signInResponse;
        try {
            signInResponse = authService.createSignInResponse(signInRequest, tokensResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Set-Cookie", refreshTokenCookie.toString())
                .body(signInResponse);
    }


}
