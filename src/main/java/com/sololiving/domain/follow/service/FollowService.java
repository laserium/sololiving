package com.sololiving.domain.follow.service;

import com.sololiving.domain.follow.dto.request.FollowRequestDto;
import com.sololiving.domain.follow.dto.response.FollowResponseDto;
import com.sololiving.domain.follow.exception.FollowErrorCode;
import com.sololiving.domain.follow.exception.FollowSuccessCode;
import com.sololiving.domain.follow.mapper.FollowMapper;
import com.sololiving.domain.follow.vo.FollowVo;
import com.sololiving.global.exception.GlobalErrorCode;
import com.sololiving.global.exception.ResponseMessage;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.exception.success.SuccessResponse;
import com.sololiving.global.security.jwt.service.TokenProvider;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowMapper followMapper;
    private final TokenProvider tokenProvider;

    @Transactional
    public void followUser(String userId, FollowRequestDto followRequestDto) {
        if (followRequestDto.getFollower() == null || followRequestDto.getFollowing() == null) {
            throw new ErrorException(GlobalErrorCode.REQUEST_IS_NULL);
        }
        // 자신을 팔로우 불가
        if (userId == followRequestDto.getFollowing()) {
            throw new ErrorException(FollowErrorCode.CANNOT_FOLLOW_MYSELF);
        }
        // 이미 팔로우 상태인지 확인

        // DB에 팔로우 정보 저장

    }

}
