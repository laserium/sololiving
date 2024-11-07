package com.sololiving.domain.article.vo;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

import com.sololiving.domain.article.enums.Status;

@Getter
@Builder(toBuilder = true)
public class ArticleVo {
    private Long articleId;
    private String writer;
    private String title;
    private String content;
    private String categoryCode;
    private int likeCnt;
    private int viewCnt;
    private int score;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
