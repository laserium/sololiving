package com.sololiving.domain.auth.service;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sololiving.domain.auth.dto.auth.request.SignInRequestDto;
import com.sololiving.domain.auth.dto.auth.response.SignInResponseDto;
import com.sololiving.domain.auth.exception.auth.AuthErrorCode;
import com.sololiving.domain.user.enums.UserType;
import com.sololiving.domain.user.service.UserAuthService;
import com.sololiving.domain.user.vo.UserVo;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.security.jwt.dto.response.CreateTokenResponse;
import com.sololiving.global.security.jwt.enums.ClientId;
import com.sololiving.global.security.jwt.exception.TokenErrorCode;
import com.sololiving.global.security.jwt.mapper.RefreshTokenMapper;
import com.sololiving.global.security.jwt.service.TokenProvider;
import com.sololiving.global.security.jwt.vo.RefreshTokenVo;
import com.sololiving.global.util.CookieService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserAuthService userAuthService;
    private final RefreshTokenMapper refreshTokenMapper;
    private final TokenProvider tokenProvider;
    private final CookieService cookieService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 로그인(RT, AT 발급)
    @Transactional
    public CreateTokenResponse createTokenResponse(SignInRequestDto signInRequest) {
        UserVo userVo = checkIdAndPwd(signInRequest);
        Duration expiresIn = TokenProvider.ACCESS_TOKEN_DURATION;

        // RefreshToken 존재 여부와 유효성 체크
        String refreshToken = getOrCreateRefreshToken(userVo, signInRequest);

        // AccessToken 생성
        String accessToken = tokenProvider.generateToken(userVo, expiresIn);

        // Response 생성 후 반환
        return CreateTokenResponse.builder()
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .expiresIn(expiresIn)
                .build();
    }

    // RefreshToken 가져오거나 새로 생성
    private String getOrCreateRefreshToken(UserVo userVo, SignInRequestDto signInRequest) {
        RefreshTokenVo refreshTokenVo = refreshTokenMapper.selectRefreshTokenByUserId(userVo.getUserId());

        // 기존 RefreshToken이 있고 유효하면 그대로 사용
        if (refreshTokenVo != null && refreshTokenVo.getExpiresIn().isAfter(LocalDateTime.now())) {
            return refreshTokenVo.getRefreshToken();
        }

        // 유효하지 않거나 없으면 새로 생성
        return tokenProvider.makeRefreshToken(userVo, signInRequest.getClientId());
    }

    // 로그아웃
    public void userSignOut(String refreshTokenValue) {
        int rowsAffected = refreshTokenMapper.deleteByRefreshToken(refreshTokenValue);
        if (rowsAffected == 0) {
            throw new ErrorException(TokenErrorCode.CANNOT_DELETE_REFRESH_TOKEN);
        }

    }

    public ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return cookieService.createRefreshTokenCookie(refreshToken);
    }

    public ResponseCookie createAccessTokenCookie(String accessToken) {
        return cookieService.createAccessTokenCookie(accessToken);
    }

    public SignInResponseDto createSignInResponse(SignInRequestDto signInRequest, CreateTokenResponse tokensResponse) {
        UserVo userVo = userAuthService.selectByUserId(signInRequest.getUserId());
        UserType userType = userVo.getUserType();
        ClientId clientId = refreshTokenMapper.selectRefreshTokenByUserId(userVo.getUserId()).getClientId();

        return SignInResponseDto.builder()
                .userType(userType)
                .clientId(clientId)
                .oauth2UserId(userVo.getOauth2UserId())
                .build();
    }

    // 비밀번호 검증
    public void verifyPassword(String passwordDb, String passwordInput) {
        if (!bCryptPasswordEncoder.matches(passwordInput, passwordDb)) {
            throw new ErrorException(AuthErrorCode.PASSWORD_INCORRECT);
        }
    }

    // 아이디와 비밀번호 체크
    private UserVo checkIdAndPwd(SignInRequestDto signInRequestDto) {
        UserVo userVo = userAuthService.selectByUserId(signInRequestDto.getUserId());
        verifyPassword(userVo.getUserPwd(), signInRequestDto.getUserPwd());
        return userVo;
    }

}
