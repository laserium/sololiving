package com.sololiving.domain.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.user.dto.response.ViewUserInfoResponseDto;
import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.domain.user.service.UserAuthService;
import com.sololiving.domain.user.service.UserViewService;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.util.SecurityUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserViewController {

    private final UserViewService userViewService;
    private final UserAuthService userAuthService;

    @GetMapping("/info")
    public ResponseEntity<ViewUserInfoResponseDto> viewUserInformation(HttpServletRequest httpServletRequest) {
        String userId = SecurityUtil.getCurrentUserId();
        if (userAuthService.isUserIdAvailable(userId)) {
            throw new ErrorException(UserErrorCode.USER_NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(userViewService.viewUserInformation(userId));
    }

}