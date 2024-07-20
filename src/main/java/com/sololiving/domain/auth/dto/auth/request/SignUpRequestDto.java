package com.sololiving.domain.auth.dto.auth.request;

import com.sololiving.global.common.enums.UserType;

import lombok.Builder;
import lombok.Getter;

// 회원가입 RequestDto
@Getter
@Builder
public class SignUpRequestDto {
    private String userId;
    private String userPwd;
    private String contact;
    private String email;
    private UserType userType;
}