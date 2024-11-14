package com.sololiving.domain.block.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.block.dto.request.BlockRequestDto;
import com.sololiving.domain.block.dto.request.UnblockRequestDto;
import com.sololiving.domain.block.exception.BlockSuccessCode;
import com.sololiving.domain.block.service.BlockService;
import com.sololiving.global.exception.ResponseMessage;
import com.sololiving.global.exception.success.SuccessResponse;
import com.sololiving.global.util.SecurityUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/block")
public class BlockController {

    private final BlockService blockService;

    // 차단 하기
    @PostMapping("")
    public ResponseEntity<SuccessResponse> block(@RequestBody BlockRequestDto requestDto,
            HttpServletRequest httpServletRequest) {
        String userId = SecurityUtil.getCurrentUserId();
        blockService.block(userId, requestDto.getTargetId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseMessage.createSuccessResponse(BlockSuccessCode.BLOCK_SUCCESS));
    }

    // 차단 해제 하기
    @DeleteMapping("")
    public ResponseEntity<SuccessResponse> unblock(@RequestBody UnblockRequestDto requestDto,
            HttpServletRequest httpServletRequest) {
        String userId = SecurityUtil.getCurrentUserId();
        blockService.unBlock(userId, requestDto.getTargetId());
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage.createSuccessResponse(BlockSuccessCode.UNBLOCK_SUCCESS));
    }

}
