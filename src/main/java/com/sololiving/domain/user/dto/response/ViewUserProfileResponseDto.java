package com.sololiving.domain.user.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ViewUserProfileResponseDto {

    private String targetId;
    private String nickname;
    private String profileImageUrl;
    private String profileBio;
    private int followerCnt;
    private int followingCnt;
    private boolean isFollowing;

    public void setIsFollowing(boolean isFollowing) {
        this.isFollowing = isFollowing;
    }

}
