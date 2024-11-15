package com.sololiving.domain.log.vo;

import com.sololiving.domain.log.enums.AuthMethod;

import lombok.Getter;

@Getter
public class AuthLogVo {
    private Long logId;
    private AuthMethod authMethod;
}
