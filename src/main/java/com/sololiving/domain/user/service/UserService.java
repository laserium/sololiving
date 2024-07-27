package com.sololiving.domain.user.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.domain.user.mapper.UserMapper;
import com.sololiving.domain.vo.UserVo;
import com.sololiving.global.exception.error.ErrorException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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
