package com.sololiving.domain.user.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sololiving.domain.user.dto.response.ViewUserListResponseDto;
import com.sololiving.domain.user.enums.UserType;
import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.domain.user.mapper.UserViewMapper;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.security.jwt.service.TokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserViewService {

    private final UserAuthService userAuthService;
    private final UserViewMapper userViewMapper;
    private final TokenProvider tokenProvider;

    public List<ViewUserListResponseDto> viewUserList(String accessToken) {
        validateViewUserList(accessToken);
        return responseViewUserList();
    }

    private boolean validateViewUserList(String accessToken) {
        String userId = tokenProvider.getUserId(accessToken);
        if (userAuthService.selectUserTypeByUserId(userId) == UserType.ADMIN) {
            return true;
        } else
            throw new ErrorException(UserErrorCode.USER_TYPE_ERROR_NO_PERMISSION);

    }

    private List<ViewUserListResponseDto> responseViewUserList() {
        return userViewMapper.selectUserList();
    }

}
