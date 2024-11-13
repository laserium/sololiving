package com.sololiving.domain.user.exception.user_profile;

import com.sololiving.global.exception.success.SuccessCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserProfileSuccessCode implements SuccessCode {

    UPDATE_PROFILE_BIO_SUCCESS("USPR_S001", "유저 프로필 설명 설정 성공"),
    UPDATE_PROFILE_IMAGE_SUCCESS("USPR_S002", "유저 프로필 이미지 설정 성공");

    private final String code;
    private final String message;

}
