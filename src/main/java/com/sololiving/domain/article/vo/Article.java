package com.sololiving.domain.article.vo;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class Article {
    private Long articleId;
    private String writer;
    private String content;
    private LocalDateTime createdAt;
    private Category category;
    private int likeCnt;
    private int viewCnt;
    private int score;
    private boolean isBlinded;
}
