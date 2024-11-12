package com.sololiving.domain.user.vo;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class UserSettingVo {

    private String userId;
    private boolean pushNotificationEnabled;
    private boolean profileSharingEnabled;
    private boolean articleSharingEnabled;
    private boolean commentSharingEnabled;
    private boolean likedSharingEnabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
