package com.sololiving.domain.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserType {
    ADMIN("관리자"),
    MANAGER("매니저"),
    GENERAL("일반");

    private final String description;

}