package com.sololiving.domain.user.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nimbusds.oauth2.sdk.token.Tokens;
import com.sololiving.domain.auth.dto.auth.request.SignUpRequestDto;
import com.sololiving.domain.auth.jwt.TokenProvider;
import com.sololiving.domain.auth.service.TokenService;
import com.sololiving.domain.user.enums.Gender;
import com.sololiving.domain.user.enums.Status;
import com.sololiving.domain.user.enums.UserType;
import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.domain.user.mapper.UserMapper;
import com.sololiving.domain.vo.UserVo;
import com.sololiving.global.exception.error.ErrorException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private static final String USER_NICK_NAME = "익명";
    private final UserAuthService userAuthService;
    private final TokenService tokenService;
    private final TokenProvider tokenProvider;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    // 회원가입
    public void signUp(SignUpRequestDto signUpRequestDto) {
        userAuthService.validateSignUpRequest(signUpRequestDto);
        saveUser(signUpRequestDto);
    }

    // 회원가입 - 저장
    @Transactional
    private void saveUser(SignUpRequestDto signUpRequestDto) {
        UserVo user = UserVo.builder()
                .userId(signUpRequestDto.getUserId())
                .userPwd(bCryptPasswordEncoder.encode(signUpRequestDto.getUserPwd()))
                .oauth2UserId(signUpRequestDto.getOauth2UserId())
                .nickname(USER_NICK_NAME)
                .contact(signUpRequestDto.getContact())
                .email(signUpRequestDto.getEmail())
                .gender(Gender.DEFAULT)
                .address(null)
                .birth(null)
                .status(Status.ACTIVE)
                .followersCnt("0")
                .followingCnt("0")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .userType(UserType.GENERAL)
                .lastSignInAt(null)
                .lastActivityAt(LocalDateTime.now())
                .build();

        userMapper.insertUser(user);
    }

    // 회원탈퇴
    @Transactional
    public void deleteUser(String accessToken, String userId) {
        if(userAuthService.validateUserIdwithAccessToken(accessToken)) {
            userMapper.deleteByUserId(userId);
        } else throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
    }

    // 회원 상태 변경
    @Transactional
    public void updateStatus(String accessToken, String userId, Status status) {
        tokenService.validateAccessToken(accessToken);
        if(userAuthService.isUserIdAvailable(userId)) {
            throw new ErrorException(UserErrorCode.ID_ALREADY_EXISTS);
        }
        userAuthService.validateStatus(status);
        String adminId = tokenProvider.getUserId(accessToken);
        if(userAuthService.findUserTypeByUserId(adminId) == UserType.ADMIN) {
            userMapper.updateUserStatus(userId, status);
        } else throw new ErrorException(UserErrorCode.USER_TYPE_ERROR_NO_PERMISSION);
    }


    
}
