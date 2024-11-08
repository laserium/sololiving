package com.sololiving.domain.follow.service;

import com.sololiving.domain.follow.dto.request.FollowRequestDto;
import com.sololiving.domain.follow.dto.request.UnfollowRequestDto;
import com.sololiving.domain.follow.exception.FollowErrorCode;
import com.sololiving.domain.follow.mapper.FollowMapper;
import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.domain.user.service.UserAuthService;
import com.sololiving.global.exception.GlobalErrorCode;
import com.sololiving.global.exception.error.ErrorException;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final UserAuthService userAuthService;
    private final FollowMapper followMapper;

    // 팔로우 하기
    public void follow(String userId, FollowRequestDto requestDto) {
        validateFollowRequest(userId, requestDto.getFollowTargetId());
        insertFollow(userId, requestDto.getFollowTargetId());
    }

    private void validateFollowRequest(String userId, String followTargetId) {
        // 입력값 NULL 유무 검증
        if (followTargetId == null) {
            throw new ErrorException(GlobalErrorCode.REQUEST_IS_NULL);
        }
        // 팔로우 하는 사람 회원유무 검증
        if (userAuthService.isUserIdAvailable(followTargetId)) {
            throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
        }
        // 본인 팔로우 불가
        if (userId.equals(followTargetId)) {
            throw new ErrorException(FollowErrorCode.CANNOT_FOLLOW_MYSELF);
        }
        // 이미 팔로우 상태인지 확인
        if (followMapper.existsFollowing(userId, followTargetId)) {
            throw new ErrorException(FollowErrorCode.ALREADY_FOLLOWED);
        }
    }

    @Transactional
    private void insertFollow(String userId, String followTargetId) {
        followMapper.insertFollow(userId, followTargetId);
    }

    // 팔로우 끊기
    public void unfollow(String userId, UnfollowRequestDto requestDto) {
        // 검증 로직 분리
        validateUnfollowRequest(userId, requestDto.getUnfollowTargetId());
        // DB 삭제 로직 실행
        deleteFollow(userId, requestDto.getUnfollowTargetId());
    }

    private void validateUnfollowRequest(String userId, String unfollowTargetId) {
        // 입력값 NULL 유무 검증
        if (unfollowTargetId == null) {
            throw new ErrorException(GlobalErrorCode.REQUEST_IS_NULL);
        }

        // 언팔로우 대상 회원 존재 여부 검증
        if (userAuthService.isUserIdAvailable(unfollowTargetId)) {
            throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
        }

        // 본인을 언팔로우할 수 없음 검증
        if (userId.equals(unfollowTargetId)) {
            throw new ErrorException(FollowErrorCode.CANNOT_UNFOLLOW_MYSELF);
        }

        // 이미 언팔로우 상태인지 확인
        if (!followMapper.existsFollowing(userId, unfollowTargetId)) {
            throw new ErrorException(FollowErrorCode.NOT_FOLLOWING);
        }
    }

    @Transactional
    private void deleteFollow(String userId, String unfollowTargetId) {
        followMapper.deleteFollow(userId, unfollowTargetId);
    }

}
