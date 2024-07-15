package com.sololiving.domain.user.enums;

public enum Gender {
    MALE("남성"),
    FEMALE("여성"),
    OTHERS("기타");

    private final String description;

    Gender(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
