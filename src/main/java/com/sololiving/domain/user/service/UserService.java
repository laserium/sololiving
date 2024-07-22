package com.sololiving.domain.user.service;

import org.springframework.stereotype.Service;

import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.domain.user.mapper.UserMapper;
import com.sololiving.domain.vo.UserVo;
import com.sololiving.global.exception.error.ErrorException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    // 아이디로 유저 찾기
    public UserVo findByUserId(String userId) {
        return userMapper.findByUserId(userId)
                .orElseThrow(() -> new ErrorException(UserErrorCode.USER_NOT_FOUND));
    }
    
}
