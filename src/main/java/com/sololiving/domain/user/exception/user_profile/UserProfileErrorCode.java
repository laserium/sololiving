package com.sololiving.domain.user.exception.user_profile;

import com.sololiving.global.exception.error.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserProfileErrorCode implements ErrorCode {
    INVALID_SETTING_TYPE("USER_PROFILE_E001", "설정 명칭이 유효하지 않습니다.");

    private final String code;
    private final String message;

}
