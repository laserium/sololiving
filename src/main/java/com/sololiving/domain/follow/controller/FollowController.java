package com.sololiving.domain.follow.controller;

import com.sololiving.domain.follow.dto.request.FollowRequestDto;
import com.sololiving.domain.follow.service.FollowService;
import com.sololiving.global.exception.success.SuccessResponse;
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

    @PostMapping("")
    public ResponseEntity<?> followUser(@RequestBody FollowRequestDto followRequestDto,
            HttpServletRequest httpServletRequest) {
        String userId = tokenProvider.getUserId(cookieService.extractAccessTokenFromCookie(httpServletRequest));
        followService.followUser(userId, followRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
