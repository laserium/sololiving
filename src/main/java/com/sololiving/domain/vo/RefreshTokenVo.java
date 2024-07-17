package com.sololiving.domain.vo;

import java.time.LocalDateTime;
import com.sololiving.domain.auth.enums.ClientId;
import com.sololiving.domain.auth.enums.TokenStatus;

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
                .refreshToken(newRefreshToken)
                .expiresIn(expiresIn)
                .issuedAt(issuedAt)
                .tokenStatus(TokenStatus.VALID)
                .updatedAt(LocalDateTime.now())
                .build();
    }

}
