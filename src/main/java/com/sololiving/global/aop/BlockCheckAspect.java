package com.sololiving.global.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.sololiving.domain.block.exception.BlockErrorCode;
import com.sololiving.domain.block.mapper.BlockMapper;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class BlockCheckAspect {

    private final BlockMapper blockMapper;

    @Before("@annotation(com.sololiving.global.aop.CheckBlockedUser) && args(targetId, ..)")
    public void checkBlockedUser(String targetId) {
        String userId = getCurrentUserId(); // 로그인된 사용자 ID 가져오기
        // 차단 여부 확인
        if (blockMapper.existsBlock(userId, targetId)) {
            throw new ErrorException(BlockErrorCode.ALREADY_BLOCKED);
        }
    }

    private String getCurrentUserId() {
        return SecurityUtil.getCurrentUserId();
    }
}