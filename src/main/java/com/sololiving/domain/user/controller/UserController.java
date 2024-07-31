package com.sololiving.domain.user.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.auth.dto.auth.request.SignUpRequestDto;
import com.sololiving.domain.auth.exception.token.TokenErrorCode;
import com.sololiving.domain.user.enums.Status;
import com.sololiving.domain.user.exception.UserSuccessCode;
import com.sololiving.domain.user.service.UserService;
import com.sololiving.global.exception.ResponseMessage;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.exception.success.SuccessResponse;
import com.sololiving.global.util.CookieService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SuccessResponse> createUser(@RequestBody SignUpRequestDto signUpRequestDto) {
        userService.signUp(signUpRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
        .body(ResponseMessage.createSuccessResponse(UserSuccessCode.SIGN_UP_SUCCESS));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<SuccessResponse> deleteUser(@PathVariable("userId") String userId, HttpServletRequest httpServletRequest) {
        String accessToken = CookieService.extractAccessTokenFromCookie(httpServletRequest);
        if(accessToken != null) {
            userService.deleteUser(accessToken, userId);
            return ResponseEntity.status(HttpStatus.OK)
            .body(ResponseMessage.createSuccessResponse(UserSuccessCode.USER_DELETE_SUCCESS));
        } else throw new ErrorException(TokenErrorCode.CANNOT_FIND_AT);
    }

    @PatchMapping("/{userId}/status/{status}")
    public ResponseEntity<?> updateUserStatus(@PathVariable("userId") String userId, @PathVariable Status status, HttpServletRequest httpServletRequest) {
        String accessToken = CookieService.extractAccessTokenFromCookie(httpServletRequest);
        userService.updateStatus(accessToken, userId, status);
        if(status == Status.ACTIVE) {
            return ResponseEntity.status(HttpStatus.OK).body(ResponseMessage.createSuccessResponse(UserSuccessCode.USER_STATUS_ACTIVE));
        } else if (status == Status.BLOCKED) {
            return ResponseEntity.status(HttpStatus.OK).body(ResponseMessage.createSuccessResponse(UserSuccessCode.USER_STATUS_BLOCKED));
        } else 
            return ResponseEntity.status(HttpStatus.OK).body(ResponseMessage.createSuccessResponse(UserSuccessCode.USER_STATUS_WITHDRAWN));
    }

    

}
