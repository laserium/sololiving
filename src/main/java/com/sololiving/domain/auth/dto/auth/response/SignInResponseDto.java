package com.sololiving.domain.auth.dto.auth.response;

import com.sololiving.domain.user.enums.UserType;
import com.sololiving.global.security.jwt.enums.ClientId;

import lombok.Builder;
import lombok.Getter;

// 로그인 ResponseDto
@Getter
@Builder
public class SignInResponseDto {
    private UserType userType;
    private ClientId clientId;
    private String oauth2UserId;

}