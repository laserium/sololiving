package com.sololiving.domain.comment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Status {
    NORMAL("정상"),
    BLIND("블라인드"),
    DELETED("삭제"),
    REPORTED("신고");

    private final String description;

}
