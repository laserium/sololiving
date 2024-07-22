package com.sololiving.global.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sololiving.domain.auth.dto.oauth.response.kakao.KakaoTokenResponseDto;
import com.sololiving.domain.auth.dto.oauth.response.naver.NaverTokenResponseDto;

@Component
public class OauthUtil {

    private static final Logger log = LoggerFactory.getLogger(OauthUtil.class);

    public void logNaverTokenResponse(NaverTokenResponseDto naverTokenResponseDto) {
        log.info(" [Kakao Service] Access Token: {}", naverTokenResponseDto.getAccessToken());
        log.info(" [Kakao Service] Refresh Token: {}", naverTokenResponseDto.getRefreshToken());
        log.info(" [Kakao Service] Expires In: {}", naverTokenResponseDto.getExpiresIn());
        log.info(" [Kakao Service] Error: {}", naverTokenResponseDto.getError());
        log.info(" [Kakao Service] Error Description: {}", naverTokenResponseDto.getErrorDescription());
    }

    public void logKakaoTokenResponse(KakaoTokenResponseDto kakaoTokenResponseDto) {
        log.info(" [Kakao Service] Access Token : {}", kakaoTokenResponseDto.getAccessToken());
        log.info(" [Kakao Service] Refresh Token : {}", kakaoTokenResponseDto.getRefreshToken());
        log.info(" [Kakao Service] Id Token : {}", kakaoTokenResponseDto.getIdToken());
        log.info(" [Kakao Service] Scope : {}", kakaoTokenResponseDto.getScope());
    }

    public static String encodeState(String state) {
        try {
            return URLEncoder.encode(state, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Failed to encode state parameter", e);
        }
    }
}
