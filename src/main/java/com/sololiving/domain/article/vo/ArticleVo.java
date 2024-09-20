package com.sololiving.domain.article.vo;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class ArticleVo {
    private Long articleId;
    private String writer;
    private String title;
    private String content;
    private Long categoryId;
    private int likeCnt;
    private int viewCnt;
    private int score;
    private boolean isBlinded;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
