package com.sololiving.domain.follow.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.follow.dto.response.ViewFollowListResponseDto;
import com.sololiving.domain.follow.dto.response.ViewFollowerListResponseDto;
import com.sololiving.domain.follow.service.FollowViewService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequiredArgsConstructor
@RequestMapping("/follow")
public class FollowViewController {

    private final FollowViewService followViewService;

    // userId가 팔로우 한 유저 조회
    @GetMapping("/follow-list/{userId}")
    public ResponseEntity<List<ViewFollowListResponseDto>> viewFollowList(@PathVariable String userId) {
        return ResponseEntity.status(HttpStatus.OK).body(followViewService.viewFollowList(userId));
    }

    // userId를 팔로우 하고있는 유저 조회
    @GetMapping("/follower-list/{userId}")
    public ResponseEntity<List<ViewFollowerListResponseDto>> viewFollowerList(@PathVariable String userId) {
        return ResponseEntity.status(HttpStatus.OK).body(followViewService.viewFollowerList(userId));
    }
}
