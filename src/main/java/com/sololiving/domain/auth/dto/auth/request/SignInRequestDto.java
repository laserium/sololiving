package com.sololiving.domain.auth.dto.auth.request;

import com.sololiving.domain.auth.enums.ClientId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

// 로그인 RequestDto
@Getter
@Builder
@AllArgsConstructor
public class SignInRequestDto {
    private String userId;
    private String userPwd;
    private ClientId clientId;
}