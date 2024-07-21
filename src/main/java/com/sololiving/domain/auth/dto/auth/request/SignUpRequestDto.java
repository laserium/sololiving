package com.sololiving.domain.auth.dto.auth.request;


import lombok.Getter;
import lombok.NoArgsConstructor;

// 회원가입 RequestDto
@Getter
@NoArgsConstructor
public class SignUpRequestDto {
    private String userId;
    private String userPwd;
    private String oauth2UserId;
    private String contact;
    private String email;
}