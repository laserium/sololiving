package com.sololiving.domain.follow.controller;

import com.sololiving.domain.follow.dto.request.FollowRequestDto;
import com.sololiving.domain.follow.dto.request.UnfollowRequestDto;
import com.sololiving.domain.follow.exception.FollowSuccessCode;
import com.sololiving.domain.follow.service.FollowService;
import com.sololiving.global.exception.ResponseMessage;
import com.sololiving.global.security.jwt.service.TokenProvider;
import com.sololiving.global.util.CookieService;

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
    private final TokenProvider tokenProvider;
    private final CookieService cookieService;

    // 팔로우 하기
    @PostMapping("")
    public ResponseEntity<?> follow(@RequestBody FollowRequestDto requestDto,
            HttpServletRequest httpServletRequest) {
        String userId = tokenProvider.getUserId(cookieService.extractAccessTokenFromCookie(httpServletRequest));
        followService.follow(userId, requestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage.createSuccessResponse(FollowSuccessCode.FOLLOW_SUCCESS));
    }

    // 팔로우 끊기
    @DeleteMapping("")
    public ResponseEntity<?> unfollow(@RequestBody UnfollowRequestDto requestDto,
            HttpServletRequest httpServletRequest) {
        String userId = tokenProvider.getUserId(cookieService.extractAccessTokenFromCookie(httpServletRequest));
        followService.unfollow(userId, requestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage.createSuccessResponse(FollowSuccessCode.UNFOLLOW_SUCCESS));
    }

}
