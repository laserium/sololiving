package com.sololiving.domain.log.vo;

import com.sololiving.domain.log.enums.BoardMethod;

import lombok.Getter;

@Getter
public class CommentLogVo {
    private Long logId;
    private Long commentId;
    private BoardMethod boardMethod;
}
