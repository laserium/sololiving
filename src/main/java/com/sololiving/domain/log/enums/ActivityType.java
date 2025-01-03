package com.sololiving.domain.log.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ActivityType {
    AUTH("인증_및_인가"),
    ARTICLE("게시글"),
    COMMENT("댓글"),
    FOLLOW("팔로우"),
    BLOCK("차단");

    private final String description;

}