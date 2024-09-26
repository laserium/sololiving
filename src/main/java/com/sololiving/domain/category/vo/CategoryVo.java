package com.sololiving.domain.category.vo;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CategoryVo {
    private String categoryCode;
    private String name;
    private String description;
    private String manager;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
