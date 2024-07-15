package com.sololiving.domain.auth.enums;

public enum TokenStatus {
    VALID("유효"), // 토큰이 유효하고 사용할 수 있는 상태.
    REVOKED("중지"), // 사용자가 로그아웃하거나 보안상의 이유로 토큰이 취소된 상태
    EXPIRED("만료"); // 토큰의 유효 기간이 만료된 상태

    private final String description;

    TokenStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }


    
}
