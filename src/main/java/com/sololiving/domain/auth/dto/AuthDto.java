package com.sololiving.domain.auth.dto;

import com.sololiving.global.common.enums.UserType;

import lombok.Getter;

public class AuthDto {
    
    // 로그인 RequestDto
    @Getter
    public static class SignInRequest {
        private String userId;
        private String userPwd;
    }

    // 회원가입 RequestDto
    @Getter
    public static class SignUpRequest {
        private String userId;
        private String userPwd;
        private String contact;
        private String email;
        private UserType userType;
}

}
