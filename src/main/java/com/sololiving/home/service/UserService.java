package com.sololiving.home.service;

import org.springframework.stereotype.Service;

import com.sololiving.home.mapper.UserMapper;
import com.sololiving.home.vo.UserVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    public void addUser(UserVO userVO) {
        userMapper.save(userVO);
    }

}