package com.sololiving.domain.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserVO {

    private String userId;
    private String userPwd;

    public UserVO(String userId, String userPwd) {
        this.userId = userId;
        this.userPwd = userPwd;
    }
}
