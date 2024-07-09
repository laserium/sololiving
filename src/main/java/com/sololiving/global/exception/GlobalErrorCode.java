package com.sololiving.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GlobalErrorCode {
    USER_NOT_FOUND("User not found");


    private final String message;
}
