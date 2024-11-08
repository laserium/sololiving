package com.sololiving.domain.follow.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowResponseDto {
    private String follower;
    private String following;
    private LocalDateTime createdAt;
    private String isAlreadyFollowing;
}