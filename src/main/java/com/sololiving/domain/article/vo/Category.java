package com.sololiving.domain.article.vo;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Category {
    private Long categoryId;
    private String name;
    private String description;
    private String manager;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
