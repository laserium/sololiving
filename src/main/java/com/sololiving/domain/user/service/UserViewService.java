package com.sololiving.domain.user.service;

import org.springframework.stereotype.Service;

import com.sololiving.domain.user.dto.response.ViewUserInfoResponseDto;
import com.sololiving.domain.user.mapper.UserViewMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserViewService {

    private final UserViewMapper userViewMapper;

    public ViewUserInfoResponseDto viewUserInformation(String userId) {
        return userViewMapper.viewUserInformation(userId);
    }

}
