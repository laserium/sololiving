package com.sololiving.domain.auth.service;

import org.springframework.stereotype.Service;

import com.sololiving.domain.auth.exception.token.TokenErrorCode;
import com.sololiving.global.exception.error.ErrorException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {

    public void validateAccessToken(String accessToken) {
    if (accessToken == null || accessToken.isEmpty()) {
        throw new ErrorException(TokenErrorCode.NO_ACCESS_TOKEN);
    }
}
    
}
