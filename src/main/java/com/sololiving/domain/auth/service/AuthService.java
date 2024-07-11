package com.sololiving.domain.auth.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sololiving.domain.auth.dto.AuthDto.SignUpRequest;
import com.sololiving.domain.auth.exception.AuthErrorCode;
import com.sololiving.domain.auth.mapper.AuthMapper;
import com.sololiving.domain.user.mapper.UserMapper;
import com.sololiving.domain.vo.UserVo;
import com.sololiving.global.common.enums.Gender;
import com.sololiving.global.common.enums.UserType;
import com.sololiving.global.exception.GlobalErrorCode;
import com.sololiving.global.exception.Exception;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String USER_NICK_NAME = "익명";

    private final AuthMapper authMapper;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 회원가입
    public void signUp(SignUpRequest signUpRequest) {
        validateSignUpRequest(signUpRequest);
        saveUser(signUpRequest);
    }

    // 회원가입 - 1. 중복데이터 검증
    private void validateSignUpRequest(SignUpRequest signUpRequest) {
        if (!isUserIdAvailable(signUpRequest.getUserId())) {
            throw new Exception(AuthErrorCode.ID_ALREADY_EXISTS);
        }
        if (!isUserEmailAvailable(signUpRequest.getEmail())) {
            throw new Exception(AuthErrorCode.EMAIL_ALREADY_EXISTS);
        }
        if (!isUserContactAvailable(signUpRequest.getContact())) {
            throw new Exception(AuthErrorCode.CONTACT_ALREADY_EXISTS);
        }
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
    private void saveUser(SignUpRequest signUpRequest) {
        UserVo user = UserVo.builder()
                .userId(signUpRequest.getUserId())
                .userPwd(bCryptPasswordEncoder.encode(signUpRequest.getUserPwd()))
                .nickName(USER_NICK_NAME)
                .contact(signUpRequest.getContact())
                .email(signUpRequest.getEmail())
                .gender(Gender.OTHERS)
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

    //
    private UserVo findByUserId(String userId) {
        UserVo userVo = userMapper.findByUserId(userId);
        if (userVo == null) {
            throw new Exception(GlobalErrorCode.USER_NOT_FOUND);
        }
        return userVo;
    }

    // 비밀번호 검증
    private void verifyPassword(UserVo userVo, String rawPassword) {
        if (!bCryptPasswordEncoder.matches(rawPassword, userVo.getUserPwd())) {
            throw new Exception(AuthErrorCode.INVALID_PASSWORD);
        }
    }

}
