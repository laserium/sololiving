package com.sololiving.domain.auth.controller;

import java.time.Duration;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.auth.dto.auth.request.SignInRequestDto;
import com.sololiving.domain.auth.dto.auth.request.SignUpRequestDto;
import com.sololiving.domain.auth.dto.auth.response.SignInResponseDto;
import com.sololiving.domain.auth.dto.token.response.CreateTokenResponse;

import com.sololiving.domain.auth.service.AuthService;

import com.sololiving.global.exception.Exception;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping("/signup")
    public ResponseEntity<?> postSignUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        authService.signUp(signUpRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("SUCCESS TO SIGNUP");
    }

    @PostMapping("/signin")
    public ResponseEntity<SignInResponseDto> postSignIn(@RequestBody SignInRequestDto signInRequestDto) {
        CreateTokenResponse tokenResponse = authService.signIn(signInRequestDto);
        ResponseCookie refreshTokenCookie = authService.createRefreshTokenCookie(tokenResponse.getRefreshToken());
        SignInResponseDto signInResponse;
        try {
            signInResponse = authService.createSignInResponse(signInRequestDto, tokenResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Set-Cookie", refreshTokenCookie.toString())
                .body(signInResponse);
    }


}
