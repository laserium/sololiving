package com.sololiving.domain.log.vo;

import com.sololiving.domain.log.enums.BoardMethod;

import lombok.Getter;

@Getter
public class ArticleLogVo {
    private Long logId;
    private Long articleId;
    private BoardMethod boardMethod;
}
