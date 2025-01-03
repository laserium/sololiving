package com.sololiving.domain.auth.service;

import org.springframework.stereotype.Service;

import com.sololiving.domain.user.vo.UserVo;
import com.sololiving.global.security.jwt.dto.response.AuthTokenResponseDto;
import com.sololiving.global.security.jwt.enums.ClientId;
import com.sololiving.global.security.jwt.service.TokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final TokenProvider tokenProvider;

    public AuthTokenResponseDto handleSignInHeader(UserVo userVo, ClientId clientId) {
        return AuthTokenResponseDto.builder()
                .accessToken(tokenProvider.generateToken(userVo, TokenProvider.ACCESS_TOKEN_DURATION))
                .refreshToken(tokenProvider.makeRefreshToken(userVo, clientId))
                .build();
    }

}
