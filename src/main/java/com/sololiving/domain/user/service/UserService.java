package com.sololiving.domain.user.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sololiving.domain.auth.dto.auth.request.SignUpRequestDto;
import com.sololiving.domain.auth.exception.AuthErrorCode;
import com.sololiving.domain.auth.jwt.TokenProvider;
import com.sololiving.domain.auth.mapper.AuthMapper;
import com.sololiving.domain.user.enums.Gender;
import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.domain.user.mapper.UserMapper;
import com.sololiving.domain.vo.UserVo;
import com.sololiving.global.common.enums.UserType;
import com.sololiving.global.exception.error.ErrorException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final String USER_NICK_NAME = "익명";
    private final AuthMapper authMapper;
    private final UserMapper userMapper;
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
            throw new ErrorException(errorCode);
        }
    }
    // 필드 유효성 검증을 위한 함수형 인터페이스
    @FunctionalInterface
    private interface Validator {
        boolean validate(String value);
    }

    // 중복 검사 - 아이디
    private boolean isUserIdAvailable(String userId) {
        return !userMapper.existsByUserId(userId);
    }

    // 중복 검사 - 이메일
    private boolean isUserEmailAvailable(String email) {
        return !userMapper.existsByEmail(email);
    }

    // 중복 검사 - 연락처
    private boolean isUserContactAvailable(String contact) {
        return !userMapper.existsByContact(contact);
    }

    // 회원가입 - 2. 저장
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
                .is_active(true)
                .followersCnt("0")
                .followingCnt("0")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .userType(UserType.GENERAL)
                .build();

        userMapper.insertUser(user);
    }

    // 회원탈퇴
    public void deleteUser(String accessToken, String userId) {
        if(validateUserId(accessToken, userId)) {
            userMapper.deleteByUserId(userId);
        } else throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
    }

    private boolean validateUserId(String accessToken, String userId) {
        String tokenUserId = tokenProvider.getUserId(accessToken);
        if(userId.equals(tokenUserId)) {
            return true;
        } else throw new ErrorException(UserErrorCode.USER_ID_INCORRECT);
    }

    // 아이디로 유저 찾기
    public UserVo findByUserId(String userId) {
        UserVo userVo = userMapper.findByUserId(userId);
        if (userVo != null) {
            return userVo;
        } else
            throw new ErrorException(UserErrorCode.USER_NOT_FOUND);
    }

    // Oauth2UserId로 유저 찾기(회원가입 분기 로직 전용)
    public UserVo findByOauth2UserId(String userId) {
        UserVo userVo = userMapper.findByOauth2UserId(userId);
        if (userVo != null) {
            return userVo;
        } else
            return null;
    }

    // 이메일로 유저 찾기
    public UserVo findByEmail(String email) {
        UserVo userVo = userMapper.findByEmail(email);
        if (userVo != null) {
            return userVo;
        } else
            throw new ErrorException(UserErrorCode.USER_NOT_FOUND);
    }

    // 아이디로 이메일 찾기
    public String findEmailByUserId(String userId) {
        String email = userMapper.findEmailByUserId(userId);
        if (email != null) {
            return email;
        } else
            throw new ErrorException(UserErrorCode.USER_EMAIL_NOT_FOUND);
    }

    // 이메일로 아이디 찾기
    public String findUserIdByEmail(String email) {
        String userId = userMapper.findUserIdByEmail(email);
        if (userId != null) {
            return userId;
        } else
            throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
    }

    // 아이디와 이메일 검증
    public String validateUserIdAndEmail(String userId, String email) {
        String userEmail = userMapper.findEmailByUserId(userId);
        if (userEmail.equals(email)) {
            return userEmail;
        } else
            throw new ErrorException(UserErrorCode.USER_EMAIL_NOT_FOUND);
    }

    // 임시 비밀번호 설정
    @Transactional
    public void setTempPassword(String userEmail, String tempPassword) {
        UserVo userVo = findByEmail(userEmail);

        userMapper.updatePassword(bCryptPasswordEncoder.encode(tempPassword), userVo.getUserId());
    }
}
