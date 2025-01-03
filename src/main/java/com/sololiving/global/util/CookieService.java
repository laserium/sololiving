package com.sololiving.global.util;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import com.sololiving.global.security.jwt.service.TokenProvider;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class CookieService {

    // RefreshToken 을 보관할 쿠키 생성
    public ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return ResponseCookie
                .from("refreshToken", refreshToken)
                .path("/")
                .httpOnly(true)
                .sameSite("none")
                .secure(true) // HTTPS 환경에서만 사용
                .maxAge(TokenProvider.REFRESH_TOKEN_DURATION.getSeconds()) // 쿠키 유효 시간 (1일)
                .build();
    }

    // AccessToken 을 보관할 쿠키 생성
    public ResponseCookie createAccessTokenCookie(String accessToken) {
        return ResponseCookie
                .from("accessToken", accessToken)
                .path("/")
                .httpOnly(true)
                .sameSite("none")
                .secure(true) // HTTPS 환경에서만 사용
                .maxAge(TokenProvider.ACCESS_TOKEN_DURATION.getSeconds()) // 쿠키 유효 시간 (30분)
                .build();
    }

    // 쿠키에서 RefreshToken 찾기
    public String extractRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    // 쿠키에서 AccessToken 찾기
    public String extractAccessTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @SuppressWarnings("null")
    public ResponseCookie deleteRefreshTokenCookie() {
        return ResponseCookie
                .from("refreshToken", null)
                .path("/")
                .httpOnly(true)
                .sameSite("none")
                .secure(true) // HTTPS 환경에서만 사용
                .maxAge(0) // 쿠키 유효 시간 0으로 설정
                .build();
    }

    @SuppressWarnings("null")
    public ResponseCookie deleteAccessTokenCookie() {
        return ResponseCookie
                .from("accessToken", null)
                .path("/")
                .httpOnly(true)
                .sameSite("none")
                .secure(true) // HTTPS 환경에서만 사용
                .maxAge(0) // 쿠키 유효 시간 0으로 설정
                .build();
    }

}
