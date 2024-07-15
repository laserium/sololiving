package com.sololiving.domain.auth.dto;

import java.time.Duration;

import com.sololiving.global.common.enums.UserType;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class AuthDto {
    
    // 로그인 RequestDto
    @Getter
    public static class SignInRequest {
        private String userId;
        private String userPwd;
    }

    // 로그인 ResponseDto
    @Getter
    @AllArgsConstructor
    public static class SignInResponse {
        private String accessTokne;
        private UserType userType;
        private Duration expiresIn;
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
