package com.sololiving.domain.follow.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sololiving.domain.follow.dto.response.ViewFollowListResponseDto;
import com.sololiving.domain.follow.dto.response.ViewFollowerListResponseDto;
import com.sololiving.domain.follow.mapper.FollowViewMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FollowViewService {

    private final FollowViewMapper followViewMapper;

    // userId가 팔로우 한 유저 조회
    public List<ViewFollowListResponseDto> viewFollowList(String userId) {
        return followViewMapper.selectFollowListByUserId(userId);
    }

    // userId를 팔로우 하고있는 유저 조회
    public List<ViewFollowerListResponseDto> viewFollowerList(String userId) {
        return followViewMapper.selectFollowerListByUserId(userId);
    }
}
