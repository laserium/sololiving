package com.sololiving.domain.block.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.block.dto.response.ViewBlockListResponseDto;
import com.sololiving.domain.block.service.BlockViewService;
import com.sololiving.global.util.SecurityUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/block")
public class BlockViewController {

    private final BlockViewService blockViewService;

    // 차단 리스트 조회
    @GetMapping("/list")
    public ResponseEntity<List<ViewBlockListResponseDto>> viewBlockList(HttpServletRequest httpServletRequest) {
        String userId = SecurityUtil.getCurrentUserId();
        return ResponseEntity.status(HttpStatus.OK).body(blockViewService.viewBlockList(userId));
    }

}
