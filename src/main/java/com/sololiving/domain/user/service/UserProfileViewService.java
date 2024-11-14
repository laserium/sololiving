package com.sololiving.domain.user.service;

import org.springframework.stereotype.Service;

import com.sololiving.domain.block.exception.BlockErrorCode;
import com.sololiving.domain.block.mapper.BlockMapper;
import com.sololiving.domain.follow.mapper.FollowMapper;
import com.sololiving.domain.user.dto.response.ViewProfileHedaerResponseDto;
import com.sololiving.domain.user.dto.response.ViewUserProfileResponseDto;
import com.sololiving.domain.user.exception.user_setting.UserSettingErrorCode;
import com.sololiving.domain.user.mapper.UserProfileViewMapper;
import com.sololiving.domain.user.mapper.UserSettingMapper;
import com.sololiving.global.exception.error.ErrorException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserProfileViewService {

    private final UserProfileViewMapper userProfileViewMapper;
    private final FollowMapper followMapper;
    private final BlockMapper blockMapper;
    private final UserSettingMapper userSettingMapper;

    // 유저 프로필 조회
    public ViewUserProfileResponseDto viewUserProfile(String userId, String targetId) {

        if (!userId.equals(targetId)) {
            // 차단 확인
            if (blockMapper.existsBlock(targetId, userId)) {
                throw new ErrorException(BlockErrorCode.YOU_ARE_BLOCKED);
            }

            // 프로필 공유 여부 확인
            if (!userSettingMapper.isProfileSharingEnabled(targetId)) {
                throw new ErrorException(UserSettingErrorCode.USER_PROFILE_SHARING_DISABLED);
            }

        }
        // 기본 프로필 정보 조회
        ViewUserProfileResponseDto profile = userProfileViewMapper.selectUserProfile(targetId);

        if (!userId.equals(targetId)) {
            // 팔로우 중일 경우 true 반환
            boolean isFollowing = followMapper.existsFollowing(userId, targetId);
            profile.setIsFollowing(isFollowing);
        }

        return profile;
    }

    // 유저 헤더 프로필 조회(유저 프로필 사진)
    public ViewProfileHedaerResponseDto viewUserProfileHeader(String userId) {
        return userProfileViewMapper.selectUserProfileHeader(userId);
    }

}
