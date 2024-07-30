package com.sololiving.domain.user.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sololiving.domain.auth.dto.auth.request.SignUpRequestDto;
import com.sololiving.domain.user.enums.Gender;
import com.sololiving.domain.user.enums.Status;
import com.sololiving.domain.user.enums.UserType;
import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.domain.user.mapper.UserMapper;
import com.sololiving.domain.vo.UserVo;
import com.sololiving.global.exception.error.ErrorException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final String USER_NICK_NAME = "익명";
    private final UserAuthService userAuthService;
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
                .nickName(USER_NICK_NAME)
                .contact(signUpRequestDto.getContact())
                .email(signUpRequestDto.getEmail())
                .gender(Gender.DEFAULT)
                .address(null)
                .birth(null)
                .status(Status.DEFAULT)
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
    public void deleteUser(String accessToken, String userId) {
        if(userAuthService.validateUserId(accessToken, userId)) {
            userMapper.deleteByUserId(userId);
        } else throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
    }

    
}
