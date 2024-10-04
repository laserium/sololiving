package com.sololiving.global.security.jwt.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sololiving.global.security.jwt.mapper.RefreshTokenMapper;
import com.sololiving.global.security.jwt.vo.RefreshTokenVo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenMapper refreshTokenMapper;

    @Transactional
    public void updateExpiredTokens() {
        List<RefreshTokenVo> expiredTokens = refreshTokenMapper.selectExpiredTokens(LocalDateTime.now());

        List<Long> expiredTokenIds = expiredTokens.stream()
                .map(RefreshTokenVo::getId)
                .collect(Collectors.toList());

        if (!expiredTokenIds.isEmpty()) {
            refreshTokenMapper.updateExpiredTokens(expiredTokenIds);
        }
    }
}
