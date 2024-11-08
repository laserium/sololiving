package com.sololiving.domain.follow.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ViewFollowListResponseDto {

    private String followerId;
    private String followerNickname;
    private String followerProfileImage;
}
