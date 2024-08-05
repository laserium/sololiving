package com.sololiving.global.security.jwt.vo;

import java.time.LocalDateTime;

import com.sololiving.global.security.jwt.enums.ClientId;
import com.sololiving.global.security.jwt.enums.TokenStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenVo {

    private Long id;
    private String userId;
    private String refreshToken;
    private LocalDateTime expiresIn;
    private LocalDateTime issuedAt; // 토큰 발급 시간
    private TokenStatus tokenStatus;
    private ClientId clientId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public RefreshTokenVo update(String newRefreshToken) {
        return RefreshTokenVo.builder()
                .userId(this.userId)
                .refreshToken(newRefreshToken)
                .expiresIn(LocalDateTime.now().plusDays(1))
                .issuedAt(this.issuedAt)
                .clientId(this.clientId)
                .tokenStatus(TokenStatus.VALID)
                .createdAt(this.createdAt)
                .updatedAt(LocalDateTime.now())
                .build();
    }

}
