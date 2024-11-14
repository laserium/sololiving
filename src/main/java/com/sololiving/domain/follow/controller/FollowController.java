package com.sololiving.domain.follow.controller;

import com.sololiving.domain.follow.dto.request.FollowRequestDto;
import com.sololiving.domain.follow.dto.request.UnfollowRequestDto;
import com.sololiving.domain.follow.exception.FollowSuccessCode;
import com.sololiving.domain.follow.service.FollowService;
import com.sololiving.global.exception.ResponseMessage;
import com.sololiving.global.exception.success.SuccessResponse;
import com.sololiving.global.util.SecurityUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/follow")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    // 팔로우 하기
    @PostMapping("")
    public ResponseEntity<SuccessResponse> follow(@RequestBody FollowRequestDto requestDto,
            HttpServletRequest httpServletRequest) {
        String userId = SecurityUtil.getCurrentUserId();
        followService.follow(userId, requestDto.getTargetId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseMessage.createSuccessResponse(FollowSuccessCode.FOLLOW_SUCCESS));
    }

    // 팔로우 끊기
    @DeleteMapping("")
    public ResponseEntity<SuccessResponse> unfollow(@RequestBody UnfollowRequestDto requestDto,
            HttpServletRequest httpServletRequest) {
        String userId = SecurityUtil.getCurrentUserId();
        followService.unfollow(userId, requestDto.getTargetId());
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage.createSuccessResponse(FollowSuccessCode.UNFOLLOW_SUCCESS));
    }

}
