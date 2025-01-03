package com.sololiving.global.security.sms.exception;

import com.sololiving.global.exception.success.SuccessCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SmsSuccessCode implements SuccessCode {

    SUCCESS_TO_SEND("SMS_S001", "문자 송신 성공"),
    CERTIFICATION_NUMBER_CORRECT("SMS_S002", "인증번호가 올바릅니다 : 인증 성공");

    private final String code;
    private final String message;

}
