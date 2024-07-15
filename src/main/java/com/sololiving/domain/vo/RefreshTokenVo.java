package com.sololiving.domain.vo;

import java.time.LocalDate;

import com.sololiving.domain.auth.enums.ClientId;
import com.sololiving.domain.auth.enums.TokenStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefreshTokenVo {

    private Long id;
    private String userId;
    private String refreshToken;
    private LocalDate expiresIn;
    private LocalDate issuedAt; // 토큰 발급 시간
    private TokenStatus tokenStatus;
    private ClientId clientId;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    public RefreshTokenVo update(String newRefreshToken) {
        return RefreshTokenVo.builder()
                .id(this.id)
                .userId(this.userId)
                .refreshToken(newRefreshToken)
                .expiresIn(this.expiresIn)
                .issuedAt(this.issuedAt)
                .tokenStatus(TokenStatus.VALID)
                .clientId(this.clientId)
                .createdAt(this.createdAt)
                .updatedAt(LocalDate.now())
                .build();
    }

}
