package com.sololiving.global.aop.admin;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.sololiving.domain.user.enums.UserType;
import com.sololiving.domain.user.mapper.UserAuthMapper;
import com.sololiving.global.exception.GlobalErrorCode;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.util.SecurityUtil;

@Aspect
@Component
@RequiredArgsConstructor
public class AdminAuthorizationAspect {

    private final UserAuthMapper userAuthMapper;

    @Before("@annotation(AdminOnly)")
    public void checkAdminPermission() {
        String userId = SecurityUtil.getCurrentUserId(); // 현재 유저 ID 가져오기
        if (userAuthMapper.selectUserTypeByUserId(userId) != UserType.ADMIN) {
            throw new ErrorException(GlobalErrorCode.NO_PERMISSION); // 권한 없을 경우 예외 발생
        }
    }
}