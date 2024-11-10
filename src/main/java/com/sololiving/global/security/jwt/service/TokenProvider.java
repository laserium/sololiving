package com.sololiving.global.security.jwt.service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sololiving.domain.user.vo.UserVo;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.security.jwt.enums.ClientId;
import com.sololiving.global.security.jwt.enums.TokenStatus;
import com.sololiving.global.security.jwt.exception.TokenErrorCode;
import com.sololiving.global.security.jwt.mapper.RefreshTokenMapper;
import com.sololiving.global.security.jwt.properties.JwtProperties;
import com.sololiving.global.security.jwt.vo.RefreshTokenVo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class TokenProvider {

    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(3);
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofMinutes(30);
    private final JwtProperties jwtProperties;
    private final RefreshTokenMapper refreshTokenMapper;

    // 토큰 생성
    public String generateToken(UserVo userVo, Duration expiresIn) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiresIn.toMillis()), userVo.getEmail(), userVo.getUserId());
    }

    public String generateTokenVer2(String userId, String email, Duration expiresIn) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiresIn.toMillis()), email, userId);
    }

    private String makeToken(Date expiry, String email, String userId) {
        Date now = new Date();
        SecretKey key = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .issuer(jwtProperties.getIssuer())
                .issuedAt(now)
                .expiration(expiry)
                .subject(email)
                .claim("id", userId)
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    // 토큰 검증
    public boolean validToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parse(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        Claims claims = getClaims(token);
        String userId = claims.get("id", String.class);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(userId, token, authorities);
    }

    // refresh token 생성
    public String makeRefreshToken(UserVo user, ClientId clientId) {
        String refreshToken = this.generateToken(user, REFRESH_TOKEN_DURATION);
        saveRefreshToken(user.getUserId(), refreshToken, clientId);

        return refreshToken;
    }

    // refresh token => DB에 저장
    @Transactional
    private void saveRefreshToken(String userId, String newRefreshToken, ClientId clientId) {
        RefreshTokenVo existingToken = refreshTokenMapper.selectRefreshTokenByUserId(userId);
        if (existingToken != null) {
            RefreshTokenVo updatedToken = existingToken.update(newRefreshToken);
            refreshTokenMapper.update(updatedToken);
        } else {
            RefreshTokenVo newToken = RefreshTokenVo.builder()
                    .userId(userId)
                    .refreshToken(newRefreshToken)
                    .expiresIn(LocalDateTime.now().plusDays(1))
                    .issuedAt(LocalDateTime.now())
                    .tokenStatus(TokenStatus.VALID)
                    .clientId(clientId)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            refreshTokenMapper.insert(newToken);
        }
    }

    // AT로 유저 아이디 추출
    public String getUserId(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new ErrorException(TokenErrorCode.NO_ACCESS_TOKEN);
        }
        Claims claims = decodeJwtToken(token);
        return claims.get("id", String.class); // Claim에서 "id" 값을 String으로 안전하게 추출
    }

    // 토큰에서 시작과 끝 따옴표 제거
    private String sanitizeToken(String token) {
        if (token != null) {
            token = token.trim(); // 공백 제거
            if (token.startsWith("\"") && token.endsWith("\"")) {
                token = token.substring(1, token.length() - 1);
            }
            if (token.startsWith("Bearer ")) { // Bearer 토큰 형식 처리
                token = token.substring(7);
            }
        }
        return token;
    }

    private Claims getClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
    }

    private Claims decodeJwtToken(String token) {
        token = sanitizeToken(token);
        SecretKey key = getSecretKey();
        return Jwts.parser()
                .verifyWith(key) // 중복 제거
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}