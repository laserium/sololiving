package com.sololiving.domain.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.auth.dto.auth.request.SignUpRequestDto;
import com.sololiving.domain.auth.exception.AuthErrorCode;
import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.domain.user.exception.UserSuccessCode;
import com.sololiving.domain.user.service.UserService;
import com.sololiving.global.exception.ResponseMessage;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.util.CookieService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user")
public class UserController {
    
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> postSignUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        userService.signUp(signUpRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
        .body(ResponseMessage.createSuccessResponse(UserSuccessCode.SIGN_UP_SUCCESS));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") String userId, HttpServletRequest httpServletRequest) {
        String accessToken = CookieService.extractAccessTokenFromCookie(httpServletRequest);
        log.info(accessToken);
        if(accessToken != null) {
            userService.deleteUser(accessToken, userId);
            return ResponseEntity.status(HttpStatus.OK)
            .body(ResponseMessage.createSuccessResponse(UserSuccessCode.USER_DELETE_SUCCESS));
        } else throw new ErrorException(AuthErrorCode.CANNOT_FIND_AT);
        
    }
}
