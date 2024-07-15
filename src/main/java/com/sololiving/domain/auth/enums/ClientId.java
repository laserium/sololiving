package com.sololiving.domain.auth.enums;

public enum ClientId {
    SOLOLIVING("자체발급"),
    KAKAO("카카오"),
    NAVER("네이버"),
    GOOGLE("구글");

    private final String description;

    ClientId(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
