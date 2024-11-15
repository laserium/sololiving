package com.sololiving.domain.log.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BlockMethod {
    BLOCK("차단"),
    UNBLOCK("차단취소");

    private final String description;

}