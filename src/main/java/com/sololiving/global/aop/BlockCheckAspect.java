package com.sololiving.global.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.sololiving.domain.block.exception.BlockErrorCode;
import com.sololiving.domain.block.mapper.BlockMapper;
import com.sololiving.global.exception.error.ErrorException;

import lombok.RequiredArgsConstructor;

// 유저 차단 기능 AOP
// 적용 메소드 : 팔로우 기능

@Aspect
@Component
@RequiredArgsConstructor
public class BlockCheckAspect {

    private final BlockMapper blockMapper;

    @Before("@annotation(com.sololiving.global.aop.CheckBlockedUser) && args(userId, targetId, ..)")
    public void checkBlockedUser(String userId, String targetId) {
        if (blockMapper.existsBlock(userId, targetId)) {
            throw new ErrorException(BlockErrorCode.ALREADY_BLOCKED);
        }
    }
}