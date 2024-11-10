package com.sololiving.domain.block.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sololiving.domain.block.exception.BlockErrorCode;
import com.sololiving.domain.block.mapper.BlockMapper;
import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.domain.user.service.UserAuthService;
import com.sololiving.global.exception.GlobalErrorCode;
import com.sololiving.global.exception.error.ErrorException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlockService {

    private final UserAuthService userAuthService;
    private final BlockMapper blockMapper;

    // 차단 하기
    public void block(String userId, String targetId) {
        validateBlockRequest(userId, targetId);
        insertBlock(userId, targetId);
    }

    private void validateBlockRequest(String userId, String targetId) {
        // 입력값 NULL 유무 검증
        if (targetId == null) {
            throw new ErrorException(GlobalErrorCode.REQUEST_IS_NULL);
        }
        // 차단 하는 사람 회원 유무 검증
        if (userAuthService.isUserIdAvailable(targetId)) {
            throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
        }
        // 본인 차단 불가
        if (userId.equals(targetId)) {
            throw new ErrorException(BlockErrorCode.CANNOT_BLOCK_MYSELF);
        }
        // 이미 차단한 상태인지 확인
        if (blockMapper.existsBlock(userId, targetId)) {
            throw new ErrorException(BlockErrorCode.ALREADY_BLOCKED);
        }
    }

    @Transactional
    private void insertBlock(String userId, String targetId) {
        blockMapper.insertBlock(userId, targetId);
    }

    // 차단 해제
    public void unBlock(String userId, String targetId) {
        validateUnblockRequest(userId, targetId);
        deleteBlock(userId, targetId);
    }

    private void validateUnblockRequest(String userId, String targetId) {
        // 입력값 NULL 유무 검증
        if (targetId == null) {
            throw new ErrorException(GlobalErrorCode.REQUEST_IS_NULL);
        }
        // 차단 하는 사람 회원 유무 검증
        if (userAuthService.isUserIdAvailable(targetId)) {
            throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
        }
        // 본인 차단 해제 불가
        if (userId.equals(targetId)) {
            throw new ErrorException(BlockErrorCode.CANNOT_UNBLOCK_MYSELF);
        }
        // 이미 차단 해제한 상태인지 확인
        if (!blockMapper.existsBlock(userId, targetId)) {
            throw new ErrorException(BlockErrorCode.DID_NOT_BLOCKED);
        }
    }

    @Transactional
    private void deleteBlock(String userId, String targetId) {
        blockMapper.deleteBlock(userId, targetId);
    }
}
