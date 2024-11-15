package com.sololiving.domain.log.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AuthMethod {
    SIGNIN("로그인"),
    SIGNOUT("로그아웃"),
    SIGNUP("회원가입"),
    WITHDRAW("회원탈퇴");

    private final String description;

}