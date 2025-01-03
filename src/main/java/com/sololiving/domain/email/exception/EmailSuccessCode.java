package com.sololiving.domain.email.exception;

import com.sololiving.global.exception.success.SuccessCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmailSuccessCode implements SuccessCode {

    EMAIL_UPDATE_CONFIRM("EMAIL_S001", "/confirmation-success");

    private final String code;
    private final String message;

}
