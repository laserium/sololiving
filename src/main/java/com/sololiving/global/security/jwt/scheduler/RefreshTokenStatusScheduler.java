package com.sololiving.global.security.jwt.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sololiving.global.security.jwt.service.RefreshTokenService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RefreshTokenStatusScheduler {

    private final RefreshTokenService refreshTokenService;

    @Scheduled(fixedRate = 1000 * 60 * 60 * 3) // 3시간마다 실행 (밀리초)
    public void scheduleTokenStatuUpdate() {
        refreshTokenService.updateExpiredTokens();
    }

}
