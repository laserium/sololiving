package com.sololiving.global.exception.success;

public interface SuccessCode {
    SuccessCode SIGN_OUT_SUCCESS = null;

    String getCode();

    String getMessage();
}
