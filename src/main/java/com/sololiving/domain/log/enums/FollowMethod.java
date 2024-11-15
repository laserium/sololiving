package com.sololiving.domain.log.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FollowMethod {
    FOLLOW("팔로우"),
    UNFOLLOW("팔로우취소");

    private final String description;

}