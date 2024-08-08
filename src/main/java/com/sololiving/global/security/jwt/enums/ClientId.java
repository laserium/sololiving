package com.sololiving.global.security.jwt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ClientId {
    SOLOLIVING("자체발급"),
    KAKAO("카카오"),
    NAVER("네이버"),
    GOOGLE("구글");

    private final String description;

}
