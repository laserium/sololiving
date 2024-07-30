package com.sololiving.domain.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Status {
    DEFAULT("기본값"),
    SIGNEDIN("로그인"),
    SIGNEDOUT("로그아웃"),
    BLOCKED("차단"),
    WITHDRAWN("탈퇴");


    private final String description;


}   