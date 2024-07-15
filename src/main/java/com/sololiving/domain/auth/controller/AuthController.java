package com.sololiving.domain.auth.controller;

import java.time.Duration;

import java.time.Duration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.auth.dto.AuthDto.SignInRequest;
import com.sololiving.domain.auth.dto.AuthDto.SignInResponse;
import com.sololiving.domain.auth.dto.AuthDto.SignUpRequest;
import com.sololiving.domain.auth.dto.TokenDto.CreateTokensResponse;
import com.sololiving.domain.auth.service.AuthService;
import com.sololiving.domain.user.service.UserService;
import com.sololiving.global.common.enums.UserType;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    
    private final AuthService authService;
    private final UserService userService;
    
    @PostMapping("/signup")
    public ResponseEntity<?> postSignUp(@RequestBody SignUpRequest signUpRequest) {
        authService.signUp(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("SUCCESS TO SIGNUP");
    }

    @PostMapping("/signin")
    public ResponseEntity<?> postSignIn(@RequestBody SignInRequest signInRequest,
        HttpServletResponse httpServletRequest) {
        CreateTokensResponse tokensResponse = authService.signIn(signInRequest);
        String refreshToken = tokensResponse.getRefreshToken();
        String accessToken = tokensResponse.getAccessToken();
        Duration expiresIn = tokensResponse.getExpiresIn();
        UserType userType = userService.findByUserId(signInRequest.getUserId()).getUserType();
        ResponseCookie cookie = ResponseCookie
                .from("refreshToken", refreshToken)
                .path("/")
                .httpOnly(true)
                .sameSite("none")
                // .secure(true) // HTTPS 환경에서만 사용
                .maxAge(24 * 60 * 60) // 쿠키 유효 시간 (예: 1일)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Set-Cookie", cookie.toString())
                .body(new SignInResponse(accessToken, userType, expiresIn));
    }
}
