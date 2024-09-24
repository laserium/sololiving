package com.sololiving.domain.category.vo;

import java.time.LocalDateTime;

import com.sololiving.domain.category.enums.Status;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryRequestVo {
    private Long id;
    private String categoryName;
    private String requestUserId;
    private Status status;
    private String reasonForRejection;
    private LocalDateTime approvedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
