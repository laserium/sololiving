package com.sololiving.domain.block.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sololiving.domain.block.dto.response.ViewBlockListResponseDto;
import com.sololiving.domain.block.mapper.BlockViewMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlockViewService {

    private final BlockViewMapper blockViewMapper;

    // 차단 목록 조회
    public List<ViewBlockListResponseDto> viewBlockList(String userId) {
        return blockViewMapper.selectBlockListByUserId(userId);
    }
}
