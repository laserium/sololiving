package com.sololiving.domain.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.user.dto.response.ViewUserSettingResponseDto;
import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.domain.user.exception.user_setting.UserSettingSuccessCode;
import com.sololiving.domain.user.service.UserAuthService;
import com.sololiving.domain.user.service.UserSettingService;
import com.sololiving.global.exception.ResponseMessage;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.exception.success.SuccessResponse;
import com.sololiving.global.util.SecurityUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/setting")
public class UserSettingController {

    private final UserSettingService userSettingService;
    private final UserAuthService userAuthService;

    // 통합된 설정 토글 서비스 호출
    @PatchMapping("/{settingType}/{status}")
    public ResponseEntity<SuccessResponse> toggleSetting(
            @PathVariable String settingType,
            @PathVariable boolean status,
            HttpServletRequest httpServletRequest) {

        String userId = SecurityUtil.getCurrentUserId();

        if (userAuthService.isUserIdAvailable(userId)) {
            throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
        }

        // 통합된 설정 토글 서비스 호출
        UserSettingSuccessCode successCode = userSettingService.toggleSetting(settingType, status, userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage.createSuccessResponse(successCode));
    }

    @GetMapping("")
    public ResponseEntity<ViewUserSettingResponseDto> viewUserSetting(HttpServletRequest httpServletRequest) {
        String userId = SecurityUtil.getCurrentUserId();

        if (userAuthService.isUserIdAvailable(userId)) {
            throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK).body(userSettingService.viewUserSetting(userId));
    }

}
