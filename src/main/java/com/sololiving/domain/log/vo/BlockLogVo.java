package com.sololiving.domain.log.vo;

import com.sololiving.domain.log.enums.BlockMethod;

import lombok.Getter;

@Getter
public class BlockLogVo {
    private Long logId;
    private String targetId;
    private BlockMethod blockMethod;
}
