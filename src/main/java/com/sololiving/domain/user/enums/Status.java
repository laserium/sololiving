package com.sololiving.domain.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Status {
    ACTIVE("활동"),
    BLOCKED("차단"),
    WITHDRAWN("탈퇴");


    private final String description;


}   