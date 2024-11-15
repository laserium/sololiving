package com.sololiving.domain.log.vo;

import java.time.LocalDateTime;

import com.sololiving.domain.log.enums.ActivityType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserActivityLogVo {

    private Long id;
    private String userId;
    private String ipAddress;
    private ActivityType activityType;
    private LocalDateTime createdAt;

}
