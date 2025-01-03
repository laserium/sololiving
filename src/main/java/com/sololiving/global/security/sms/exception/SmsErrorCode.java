package com.sololiving.global.security.sms.exception;

import com.sololiving.global.exception.error.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SmsErrorCode implements ErrorCode {
    CERTIFICATION_NUMBER_INCORRECT("SMS_E001", "인증번호가 다릅니다 : 인증 실패");

    private final String code;
    private final String message;

}
