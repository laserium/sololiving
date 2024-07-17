package com.sololiving.domain.auth.dto;

import java.time.Duration;


import com.sololiving.domain.auth.enums.ClientId;
import com.sololiving.global.common.enums.UserType;

import lombok.Builder;
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
    @Builder
    public static class SignInResponse {
        private String accessToken;
        private Duration expiresIn;
        private UserType userType;
        private ClientId clientId;
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
