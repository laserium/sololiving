package com.sololiving.domain.auth.dto.auth.request;

import lombok.Builder;
import lombok.Getter;

// 로그인 RequestDto
@Getter
@Builder
public class SignInRequestDto {
    private String userId;
    private String userPwd;
}