package com.sololiving.domain.follow.service;

import com.sololiving.domain.follow.dto.request.FollowRequestDto;
import com.sololiving.domain.follow.dto.request.UnfollowRequestDto;
import com.sololiving.domain.follow.exception.FollowErrorCode;
import com.sololiving.domain.follow.mapper.FollowMapper;
import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.domain.user.service.UserAuthService;
import com.sololiving.global.aop.CheckBlockedUser;
import com.sololiving.global.exception.GlobalErrorCode;
import com.sololiving.global.exception.error.ErrorException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final UserAuthService userAuthService;
    private final FollowMapper followMapper;

    // 팔로우 하기
    @CheckBlockedUser
    public void follow(String userId, String targetId) {
        validateFollowRequest(userId, targetId);
        insertFollow(userId, targetId);
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
    }

    // 팔로우 끊기
    public void unfollow(String userId, String targetId) {
        // 검증 로직 분리
        validateUnfollowRequest(userId, targetId);
        // DB 삭제 로직 실행
        deleteFollow(userId, targetId);
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
