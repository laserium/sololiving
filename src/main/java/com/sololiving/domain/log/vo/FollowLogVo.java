package com.sololiving.domain.log.vo;

import com.sololiving.domain.log.enums.FollowMethod;

import lombok.Getter;

@Getter
public class FollowLogVo {
    private Long logId;
    private String targetId;
    private FollowMethod followMethod;
}
