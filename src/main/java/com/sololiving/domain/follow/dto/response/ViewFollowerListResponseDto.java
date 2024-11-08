package com.sololiving.domain.follow.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ViewFollowerListResponseDto {

    private String followerId;
    private String followerNickname;
    private String followerProfileImage;
}