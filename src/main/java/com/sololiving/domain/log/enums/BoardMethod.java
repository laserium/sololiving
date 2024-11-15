package com.sololiving.domain.log.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BoardMethod {
    CREATE("생성"),
    DELETE("삭제"),
    VIEW("조회"),
    UPDATE("수정"),
    LIKE("추천"),
    UNLIKE("추천취소");

    private final String description;

}