package com.sololiving.home.dto;

import lombok.Getter;

public class UserDto {
    
    // 회원가입 RequestDto
    @Getter
    public static class SignUpRequest {
        private String userId;
        private String userPwd;
    }

    // 로그인 RequestDto
    @Getter 
    public static class SignInRequest {
        private String userId;
        private String userPwd;
    }

}
