package com.sololiving.domain.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.user.exception.UserSuccessCode;
import com.sololiving.global.exception.ResponseMessage;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/profile")
public class UserProfileController {

    @PostMapping("/image")
    public ResponseEntity<?> insertProfileImage(HttpServletRequest httpServletRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage
                        .createSuccessResponse(UserSuccessCode.USER_DELETE_SUCCESS));
    }

    @PostMapping("/bio")
    public ResponseEntity<?> insertBioImage(@RequestBody String entity) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage
                        .createSuccessResponse(UserSuccessCode.USER_DELETE_SUCCESS));
    }

}
