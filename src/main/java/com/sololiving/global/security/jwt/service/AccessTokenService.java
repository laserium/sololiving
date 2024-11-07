package com.sololiving.global.security.jwt.service;

import org.springframework.stereotype.Service;

import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.security.jwt.exception.TokenErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccessTokenService {

    public void checkAccessToken(String accessToken) {
        if (accessToken == null || accessToken.isEmpty()) {
            throw new ErrorException(TokenErrorCode.NO_ACCESS_TOKEN);
        }
    }

}
