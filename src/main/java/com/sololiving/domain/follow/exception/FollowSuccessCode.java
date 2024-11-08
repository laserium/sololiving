package com.sololiving.domain.follow.exception;

import com.sololiving.global.exception.success.SuccessCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FollowSuccessCode implements SuccessCode {

    FOLLOW_SUCCESS("FOLLOW_S001", "팔로우 성공"),
    UNFOLLOW_SUCCESS("FOLLOW_S002", "팔로우 삭제 완료");

    private final String code;
    private final String message;

}