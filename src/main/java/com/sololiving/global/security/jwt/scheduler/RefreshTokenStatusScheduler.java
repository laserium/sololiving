package com.sololiving.global.security.jwt.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sololiving.global.security.jwt.service.RefreshTokenService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RefreshTokenStatusScheduler {

    private final RefreshTokenService refreshTokenService;

    // 토큰 만료 업데이트 스케쥴링 : 3시간 마다
    @Scheduled(fixedRate = 1000 * 60 * 60 * 3)
    public void scheduleTokenStatuUpdate() {
        refreshTokenService.updateExpiredTokens();
    }

}
