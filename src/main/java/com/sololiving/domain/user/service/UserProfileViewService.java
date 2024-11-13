package com.sololiving.domain.user.service;

import org.springframework.stereotype.Service;

import com.sololiving.domain.follow.mapper.FollowMapper;
import com.sololiving.domain.user.dto.response.ViewUserProfileResponseDto;
import com.sololiving.domain.user.mapper.UserProfileViewMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserProfileViewService {

    private final UserProfileViewMapper userProfileViewMapper;
    private final FollowMapper followMapper;

    public ViewUserProfileResponseDto getUserProfile(String userId, String targetId) {
        // 기본 프로필 정보 조회
        ViewUserProfileResponseDto profile = userProfileViewMapper.selectUserProfile(targetId);

        if (!userId.equals(targetId)) {
            // 팔로우 중일 경우 true 반환
            boolean isFollowing = followMapper.existsFollowing(userId, targetId);
            profile.setIsFollowing(isFollowing);
        }

        return profile;
    }

}
