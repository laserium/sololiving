package com.sololiving.domain.article.vo;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ArticleLikeVo {

    private Long articleId;
    private String userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
