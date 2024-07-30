package com.sololiving.domain.user.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sololiving.domain.user.dto.response.ViewUserListResponseDto;
import com.sololiving.domain.user.enums.UserType;
import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.domain.user.mapper.UserViewMapper;
import com.sololiving.global.exception.error.ErrorException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserViewService {

    private final UserAuthService userAuthService;
    private final UserViewMapper userViewMapper;

    public List<ViewUserListResponseDto> viewUserList(String accessToken, String userId) {
        validateViewUserList(accessToken, userId);
        return responseViewUserList();
    }

    private boolean validateViewUserList(String accessToken, String userId) {
        if(userAuthService.validateUserId(accessToken, userId)) {
            if(userAuthService.findUserTypeByUserId(userId) == UserType.ADMIN) {
                return true;
            } else throw new ErrorException(UserErrorCode.USER_TYPE_ERROR_NO_PERMISSION);
        } else throw new ErrorException(UserErrorCode.USER_ID_INCORRECT);

    }
    
    private List<ViewUserListResponseDto> responseViewUserList() {
        return userViewMapper.findUserList();
    }

}
