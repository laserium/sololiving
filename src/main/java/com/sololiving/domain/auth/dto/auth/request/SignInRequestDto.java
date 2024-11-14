package com.sololiving.domain.auth.dto.auth.request;

import com.sololiving.global.security.jwt.enums.ClientId;

import lombok.Getter;
import lombok.NoArgsConstructor;

// 로그인 RequestDto
@Getter
@NoArgsConstructor
public class SignInRequestDto {
    private String userId;
    private String userPwd;
    private ClientId clientId;
}