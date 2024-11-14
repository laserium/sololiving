package com.sololiving.domain.user.vo;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfileImageVo {
    private String userId;
    private String imageUrl;
    private String fileName;
    private long fileSize;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
