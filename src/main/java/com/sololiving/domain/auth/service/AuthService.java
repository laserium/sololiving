package com.sololiving.domain.auth.service;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sololiving.domain.auth.dto.auth.request.SignInRequestDto;
import com.sololiving.domain.auth.dto.auth.request.SignUpRequestDto;
import com.sololiving.domain.auth.dto.auth.response.SignInResponseDto;
import com.sololiving.domain.auth.dto.token.response.CreateTokenResponse;
import com.sololiving.domain.auth.enums.ClientId;
import com.sololiving.domain.auth.exception.AuthErrorCode;
import com.sololiving.domain.auth.jwt.TokenProvider;
import com.sololiving.domain.auth.mapper.AuthMapper;
import com.sololiving.domain.auth.mapper.RefreshTokenMapper;
import com.sololiving.domain.auth.util.CookieUtil;
import com.sololiving.domain.user.enums.Gender;
import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.domain.user.mapper.UserMapper;
import com.sololiving.domain.user.service.UserService;
import com.sololiving.domain.vo.RefreshTokenVo;
import com.sololiving.domain.vo.UserVo;
import com.sololiving.global.common.enums.UserType;
import com.sololiving.global.exception.Exception;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String USER_NICK_NAME = "익명";

    private final UserService userService;
    private final AuthMapper authMapper;
    private final UserMapper userMapper;
    private final RefreshTokenMapper refreshTokenMapper;
    private final TokenProvider tokenProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 회원가입
    public void signUp(SignUpRequestDto signUpRequestDto) {
        validateSignUpRequest(signUpRequestDto);
        saveUser(signUpRequestDto);
    }

    // 회원가입 - 1. 중복데이터 검증
    private void validateSignUpRequest(SignUpRequestDto signUpRequestDto) {
        validateUniqueField(signUpRequestDto.getUserId(), this::isUserIdAvailable, AuthErrorCode.ID_ALREADY_EXISTS);
        validateUniqueField(signUpRequestDto.getEmail(), this::isUserEmailAvailable, AuthErrorCode.EMAIL_ALREADY_EXISTS);
        validateUniqueField(signUpRequestDto.getContact(), this::isUserContactAvailable, AuthErrorCode.CONTACT_ALREADY_EXISTS);
    }
    // 필드의 유일성 검증
    private void validateUniqueField(String fieldValue, Validator validator, AuthErrorCode errorCode) {
        if (!validator.validate(fieldValue)) {
            throw new Exception(errorCode);
        }
    }
    // 필드 유효성 검증을 위한 함수형 인터페이스
    @FunctionalInterface
    private interface Validator {
        boolean validate(String value);
    }

    // 중복 검사 - 아이디
    private boolean isUserIdAvailable(String userId) {
        return !authMapper.existsByUserId(userId);
    }

    // 중복 검사 - 이메일
    private boolean isUserEmailAvailable(String email) {
        return !authMapper.existsByEmail(email);
    }

    // 중복 검사 - 연락처
    private boolean isUserContactAvailable(String contact) {
        return !authMapper.existsByContact(contact);
    }

    // 회원가입 - 2. 저장
    @Transactional
    private void saveUser(SignUpRequestDto signUpRequestDto) {
        UserVo user = UserVo.builder()
                .userId(signUpRequestDto.getUserId())
                .userPwd(bCryptPasswordEncoder.encode(signUpRequestDto.getUserPwd()))
                .nickName(USER_NICK_NAME)
                .contact(signUpRequestDto.getContact())
                .email(signUpRequestDto.getEmail())
                .gender(Gender.DEFAULT)
                .address(null)
                .birth(null)
                .is_active(true)
                .followersCnt("0")
                .followingCnt("0")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .userType(UserType.GENERAL)
                .build();

        authMapper.insertUser(user);
    }

    // 로그인(RT, AT 발급)
    @Transactional
    public CreateTokenResponse signIn(SignInRequestDto signInRequest) {
        // 아이디와 비밀번호 체크
        UserVo userVo = userMapper.findByUserId(signInRequest.getUserId()).orElseThrow(() -> new Exception(UserErrorCode.USER_NOT_FOUND));
        this.verifyPassword(userVo, signInRequest.getUserPwd());
        // Refresh Token 발급 + DB에 저장
        String refreshToken = tokenProvider.makeRefreshToken(userVo);       
        Duration expiresIn = Duration.ofMinutes(30);
        // Duration expiresIn = Duration.ofSeconds(10);
        String accessToken = tokenProvider.generateToken(userVo, expiresIn);
        return CreateTokenResponse.builder()
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .expiresIn(expiresIn)
                .build();
    }

    public ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return CookieUtil.createRefreshTokenCookie(refreshToken);
    }

    public SignInResponseDto createSignInResponse(SignInRequestDto signInRequest, CreateTokenResponse tokensResponse) {
        String accessToken = tokensResponse.getAccessToken();
        Duration expiresIn = tokensResponse.getExpiresIn();
        UserVo userVo = userService.findByUserId(signInRequest.getUserId());
        UserType userType = userVo.getUserType();
        ClientId clientId = refreshTokenMapper.findRefreshTokenByUserId(userVo.getUserId())
                                              .map(RefreshTokenVo::getClientId)
                                              .orElseThrow(() -> new Exception(AuthErrorCode.CANNOT_FIND_RT));
        
        return SignInResponseDto.builder()
                .accessToken(accessToken)
                .expiresIn(expiresIn)
                .userType(userType)
                .clientId(clientId)
                .build();
    }

    // 비밀번호 검증
    private void verifyPassword(UserVo userVo, String password) {
        if (!bCryptPasswordEncoder.matches(password, userVo.getUserPwd())) {
            throw new Exception(AuthErrorCode.PASSWORD_INCORRECT);
        }
    }


}
