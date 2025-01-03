package com.sololiving.domain.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sololiving.domain.user.dto.request.UpdateUserProfileBioRequestDto;
import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.domain.user.exception.user_profile.UserProfileSuccessCode;
import com.sololiving.domain.user.service.UserAuthService;
import com.sololiving.domain.user.service.UserProfileService;
import com.sololiving.global.exception.ResponseMessage;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.exception.success.SuccessResponse;
import com.sololiving.global.util.SecurityUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/profile")
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final UserAuthService userAuthService;

    @PutMapping("/image")
    public ResponseEntity<SuccessResponse> uploadProfileImage(@RequestParam("file") MultipartFile file,
            HttpServletRequest httpServletRequest) {
        String userId = SecurityUtil.getCurrentUserId();
        if (userAuthService.isUserIdAvailable(userId)) {
            throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
        }
        userProfileService.uploadProfileImage(userId, file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage
                        .createSuccessResponse(UserProfileSuccessCode.UPDATE_PROFILE_IMAGE_SUCCESS));
    }

    @PatchMapping("/bio")
    public ResponseEntity<SuccessResponse> updateUserBio(@RequestBody UpdateUserProfileBioRequestDto requestDto,
            HttpServletRequest httpServletRequest) {
        String userId = SecurityUtil.getCurrentUserId();
        if (userAuthService.isUserIdAvailable(userId)) {
            throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage
                        .createSuccessResponse(UserProfileSuccessCode.UPDATE_PROFILE_BIO_SUCCESS));
    }

}
