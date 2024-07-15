package com.sololiving.global.common.enums;

public enum UserType {
    ADMIN("관리자"),
    GENERAL("일반");

    private final String description;

    UserType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}   