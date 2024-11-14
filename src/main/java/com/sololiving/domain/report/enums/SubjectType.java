package com.sololiving.domain.report.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SubjectType {

    ARTICLE("게시판"),
    COMMENT("댓글");

    private final String description;

}
