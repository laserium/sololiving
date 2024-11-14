package com.sololiving.domain.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.user.dto.response.ViewProfileHedaerResponseDto;
import com.sololiving.domain.user.dto.response.ViewUserProfileResponseDto;
import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.domain.user.service.UserAuthService;
import com.sololiving.domain.user.service.UserProfileViewService;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.util.SecurityUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/profile")
public class UserProfileViewController {

    private final UserProfileViewService userProfileViewService;
    private final UserAuthService userAuthService;

    // 유저 프로필 조회(마이페이지)
    @GetMapping("/{targetId}")
    public ResponseEntity<ViewUserProfileResponseDto> viewUserProfile(@PathVariable String targetId) {
        String userId = SecurityUtil.getCurrentUserId();
        if (userAuthService.isUserIdAvailable(targetId)) {
            throw new ErrorException(UserErrorCode.USER_NOT_FOUND);
        }
        ViewUserProfileResponseDto profile = userProfileViewService.viewUserProfile(userId, targetId);
        return ResponseEntity.status(HttpStatus.OK).body(profile);
    }

    // 유저 헤더 프로필 조회(헤더)
    @GetMapping("/header")
    public ResponseEntity<ViewProfileHedaerResponseDto> viewUserProfileHeader(HttpServletRequest httpServletRequest) {
        String userId = SecurityUtil.getCurrentUserId();
        if (userAuthService.isUserIdAvailable(userId)) {
            throw new ErrorException(UserErrorCode.USER_NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK).body(userProfileViewService.viewUserProfileHeader(userId));
    }

}
