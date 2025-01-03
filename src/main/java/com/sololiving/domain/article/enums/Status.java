package com.sololiving.domain.article.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Status {
    NORMAL("정상"),
    BLIND("블라인드"),
    DELETED("삭제");

    private final String description;

}