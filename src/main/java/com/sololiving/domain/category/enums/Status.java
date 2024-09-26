package com.sololiving.domain.category.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Status {
    PENDING("심사중"),
    APPROVED("승인"),
    REJECTED("거절");

    private final String description;

}
