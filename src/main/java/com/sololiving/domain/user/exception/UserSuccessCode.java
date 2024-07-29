package com.sololiving.domain.user.exception;

import com.sololiving.global.exception.success.SuccessCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserSuccessCode implements SuccessCode {

    SIGN_UP_SUCCESS("USER_S001", "회원가입에 성공하셨습니다."),
    USER_DELETE_SUCCESS("USER_S002", "회원탈퇴에 성공하셨습니다.");

    private final String code;
    private final String message;
    
}
