package com.sololiving.domain.follow.vo;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowVo {

    private String follower;
    private String following;
    private LocalDateTime createdAt;
    private String isAlreadyFollowing;

}