package com.sololiving.domain.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Gender {
    MALE("남성"),
    FEMALE("여성"),
    DEFAULT("선택");

    private final String description;


}
