package com.sololiving.domain.auth.util;

import java.time.Duration;

import org.springframework.http.ResponseCookie;

public class CookieUtil {
    
        public static ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return ResponseCookie
                .from("refreshToken", refreshToken)
                .path("/")
                .httpOnly(true)
                .sameSite("none")
                // .secure(true) // HTTPS 환경에서만 사용
                .maxAge(Duration.ofDays(1).getSeconds()) // 쿠키 유효 시간 (1일)
                .build();
    }
}
