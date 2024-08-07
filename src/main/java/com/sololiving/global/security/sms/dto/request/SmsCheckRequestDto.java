package com.sololiving.global.security.sms.dto.request;

import lombok.Getter;

// 회원가입 문자 인증 확인 RequestDto
@Getter
public class SmsCheckRequestDto {
    private String contact;
    private String code;
}