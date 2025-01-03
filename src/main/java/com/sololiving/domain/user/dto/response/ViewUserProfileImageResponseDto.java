package com.sololiving.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ViewUserProfileImageResponseDto {
    private String userId;
    private String imageUrl;
    private String fileName;
    private long fileSize;
}
