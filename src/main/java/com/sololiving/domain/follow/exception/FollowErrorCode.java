package com.sololiving.domain.follow.exception;

import com.sololiving.global.exception.error.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FollowErrorCode implements ErrorCode {

    CANNOT_FOLLOW_MYSELF("FOLLOW_E001", "자기 자신을 팔로우할 수 없습니다."),
    ALREADY_FOLLOWED("FOLLOW_E002", "이미 팔로우한 사용자입니다."),
    NOT_FOLLOWING("FOLLOW_E003", "팔로잉 상태가 아닌 사용자입니다."),
    CANNOT_UNFOLLOW_MYSELF("FOLLOW_E004", "자기 자신을 언팔로우할 수 없습니다.");

    private final String code;
    private final String message;
}
