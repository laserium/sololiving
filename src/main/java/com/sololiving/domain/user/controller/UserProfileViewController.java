package com.sololiving.domain.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.user.dto.response.ViewUserProfileResponseDto;
import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.domain.user.service.UserAuthService;
import com.sololiving.domain.user.service.UserProfileViewService;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.util.SecurityUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/profile")
public class UserProfileViewController {

    private final UserProfileViewService userProfileViewService;
    private final UserAuthService userAuthService;

    @GetMapping("/{targetId}")
    public ResponseEntity<ViewUserProfileResponseDto> getUserProfile(@PathVariable String targetId) {
        String userId = SecurityUtil.getCurrentUserId();
        if (userAuthService.isUserIdAvailable(targetId)) {
            throw new ErrorException(UserErrorCode.USER_NOT_FOUND);
        }
        ViewUserProfileResponseDto profile = userProfileViewService.getUserProfile(userId, targetId);
        return ResponseEntity.status(HttpStatus.OK).body(profile);
    }

    @GetMapping("/header")
    public String getMethodName(@RequestParam String param) {
        return new String();
    }

}
