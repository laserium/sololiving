package com.sololiving.domain.auth.service;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sololiving.domain.auth.dto.auth.request.SignInRequestDto;
import com.sololiving.domain.auth.dto.auth.response.SignInResponseDto;
import com.sololiving.domain.auth.dto.token.response.CreateTokenResponse;
import com.sololiving.domain.auth.enums.ClientId;
import com.sololiving.domain.auth.exception.auth.AuthErrorCode;
import com.sololiving.domain.auth.exception.token.TokenErrorCode;
import com.sololiving.domain.auth.jwt.TokenProvider;
import com.sololiving.domain.auth.mapper.RefreshTokenMapper;
import com.sololiving.domain.user.enums.UserType;
import com.sololiving.domain.user.service.UserAuthService;
import com.sololiving.domain.vo.RefreshTokenVo;
import com.sololiving.domain.vo.UserVo;
import com.sololiving.global.exception.error.ErrorException;
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
        RefreshTokenVo refreshTokenVo = refreshTokenMapper.findRefreshTokenByUserId(userVo.getUserId());
        String refreshToken;
        if (refreshTokenVo != null) {
            if(refreshTokenVo.getExpiresIn().isAfter(LocalDateTime.now())) {
                refreshToken = refreshTokenVo.getRefreshToken();
            } else refreshToken = refreshTokenVo.getRefreshToken();
        } else {
            refreshToken = tokenProvider.makeRefreshToken(userVo, signInRequest.getClientId());
        }
        String accessToken = tokenProvider.generateToken(userVo, expiresIn);
        return CreateTokenResponse.builder()
                                  .refreshToken(refreshToken)
                                  .accessToken(accessToken)
                                  .expiresIn(expiresIn)
                                  .build();
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
        Duration expiresIn = tokensResponse.getExpiresIn();
        UserVo userVo = userAuthService.findByUserId(signInRequest.getUserId());
        UserType userType = userVo.getUserType();
        ClientId clientId = refreshTokenMapper.findRefreshTokenByUserId(userVo.getUserId()).getClientId();
        
        return SignInResponseDto.builder()
                .expiresIn(expiresIn)
                .userType(userType)
                .clientId(clientId)
                .oauth2UserId(userVo.getOauth2UserId())
                .build();
    }

    // 비밀번호 검증
    private void verifyPassword(UserVo userVo, String password) {
        if (!bCryptPasswordEncoder.matches(password, userVo.getUserPwd())) {
            throw new ErrorException(AuthErrorCode.PASSWORD_INCORRECT);
        }
    }
    // 아이디와 비밀번호 체크
    private UserVo checkIdAndPwd(SignInRequestDto signInRequestDto) {
        UserVo userVo = userAuthService.findByUserId(signInRequestDto.getUserId());
        verifyPassword(userVo, signInRequestDto.getUserPwd());
        return userVo;
    }
    
    
    
    
}
