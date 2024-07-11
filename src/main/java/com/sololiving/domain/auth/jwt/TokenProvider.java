package com.sololiving.domain.auth.jwt;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.nimbusds.oauth2.sdk.token.RefreshToken;
import com.sololiving.domain.auth.mapper.RefreshTokenMapper;
import com.sololiving.domain.vo.UserVo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TokenProvider {

    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(1);
    private final JwtProperties jwtProperties;
    private final RefreshTokenMapper refreshTokenMapper;

    // 토큰 생성
    public String generateToken(UserVo user, Duration expiredAt) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }

    private String makeToken(Date expiry, UserVo user) {
        Date now = new Date();
        SecretKey key = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .issuer(jwtProperties.getIssuer())
                .issuedAt(now)
                .expiration(expiry)
                .subject(user.getEmail())
                .claim("id", user.getUserId())
                .signWith(key, Jwts.SIG.HS512)
                .compact();
    }

    public boolean validToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
            Jwts.parser()
                    .decryptWith(key)
                    .build()
                    .parse(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(
                new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities), token,
                authorities);
    }

    // refresh token 생성
    public String makeRefreshToken(UserVo user) {
        String refreshToken = this.generateToken(user, REFRESH_TOKEN_DURATION);
        // saveRefreshToken(user.getUserId(), refreshToken);
        return refreshToken;
    }

    // refresh token => DB에 저장
    private void saveRefreshToken(String userId, String newRefreshToken) {
        RefreshToken refreshtoken = refreshTokenMapper.findByUserId(userId)
                .map(entity -> entity.update(newRefreshToken))
                .orElse(new RefreshToken(userId, newRefreshToken));
        refreshTokenMapper.save(refreshtoken);
    }

    // AT로 유저 아이디 추출
    public String getUserId(String token) {
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
                .decryptWith(key)
                .build()
                .parseEncryptedClaims(token)
                .getPayload();
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
    }

    private Claims decodeJwtToken(String token) {
        token = sanitizeToken(token);
        SecretKey key = getSecretKey();
        return Jwts.parser()
                .decryptWith(key) // 중복 제거
                .build()
                .parseEncryptedClaims(token)
                .getPayload();
    }

}