package com.sololiving.domain.follow.service;

import com.sololiving.domain.alarm.service.AlarmService;
import com.sololiving.domain.follow.exception.FollowErrorCode;
import com.sololiving.domain.follow.mapper.FollowMapper;
import com.sololiving.domain.log.enums.FollowMethod;
import com.sololiving.domain.log.service.UserActivityLogService;
import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.domain.user.mapper.UserSettingMapper;
import com.sololiving.domain.user.service.UserAuthService;
import com.sololiving.global.aop.block.CheckBlockedUser;
import com.sololiving.global.exception.GlobalErrorCode;
import com.sololiving.global.exception.error.ErrorException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final UserAuthService userAuthService;
    private final UserSettingMapper userSettingMapper;
    private final AlarmService alarmService;
    private final FollowMapper followMapper;
    private final UserActivityLogService userActivityLogService;

    // 팔로우 하기
    @CheckBlockedUser
    public void follow(String userId, String targetId, String ipAddress) {
        validateFollowRequest(userId, targetId);
        insertFollow(userId, targetId);

        // 사용자 행동 로그 처리
        userActivityLogService.insertFollowLog(userId, ipAddress, targetId, FollowMethod.FOLLOW);
    }

    private void validateFollowRequest(String userId, String targetId) {
        // 입력값 NULL 유무 검증
        if (targetId == null) {
            throw new ErrorException(GlobalErrorCode.REQUEST_IS_NULL);
        }
        // 팔로우 하는 사람 회원유무 검증
        if (userAuthService.isUserIdAvailable(targetId)) {
            throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
        }
        // 본인 팔로우 불가
        if (userId.equals(targetId)) {
            throw new ErrorException(FollowErrorCode.CANNOT_FOLLOW_MYSELF);
        }
        // 이미 팔로우 상태인지 확인
        if (followMapper.existsFollowing(userId, targetId)) {
            throw new ErrorException(FollowErrorCode.ALREADY_FOLLOWED);
        }
    }

    @Transactional
    private void insertFollow(String userId, String targetId) {
        followMapper.insertFollow(userId, targetId);

        if (userSettingMapper.isPushNotificationSharingEnabled(targetId)) {
            alarmService.addFollowAlarm(targetId, userId);
        }

    }

    // 팔로우 끊기
    public void unfollow(String userId, String targetId, String ipAddress) {
        validateUnfollowRequest(userId, targetId);
        deleteFollow(userId, targetId);

        // 사용자 행동 로그 처리
        userActivityLogService.insertFollowLog(userId, ipAddress, targetId, FollowMethod.UNFOLLOW);
    }

    private void validateUnfollowRequest(String userId, String targetId) {
        // 입력값 NULL 유무 검증
        if (targetId == null) {
            throw new ErrorException(GlobalErrorCode.REQUEST_IS_NULL);
        }

        // 언팔로우 대상 회원 존재 여부 검증
        if (userAuthService.isUserIdAvailable(targetId)) {
            throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
        }

        // 본인을 언팔로우할 수 없음 검증
        if (userId.equals(targetId)) {
            throw new ErrorException(FollowErrorCode.CANNOT_UNFOLLOW_MYSELF);
        }

        // 이미 언팔로우 상태인지 확인
        if (!followMapper.existsFollowing(userId, targetId)) {
            throw new ErrorException(FollowErrorCode.NOT_FOLLOWING);
        }
    }

    @Transactional
    private void deleteFollow(String userId, String targetId) {
        followMapper.deleteFollow(userId, targetId);
    }

}
