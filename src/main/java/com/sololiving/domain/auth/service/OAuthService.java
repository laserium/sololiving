package com.sololiving.domain.auth.service;

import org.springframework.stereotype.Service;

import com.sololiving.domain.auth.dto.token.response.AuthTokenResponseDto;
import com.sololiving.domain.auth.enums.ClientId;
import com.sololiving.domain.auth.jwt.TokenProvider;
import com.sololiving.domain.vo.UserVo;

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
