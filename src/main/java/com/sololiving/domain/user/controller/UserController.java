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
import com.sololiving.domain.user.dto.response.ViewUserListResponseDto;
import com.sololiving.domain.user.exception.UserSuccessCode;
import com.sololiving.domain.user.service.UserService;
import com.sololiving.domain.user.service.UserViewService;
import com.sololiving.global.exception.ResponseMessage;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.exception.success.SuccessResponse;
import com.sololiving.global.util.CookieService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    
    private final UserService userService;
    private final UserViewService userViewService;

    @PostMapping("/signup")
    public ResponseEntity<SuccessResponse> postSignUp(@RequestBody SignUpRequestDto signUpRequestDto) {
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
        } else throw new ErrorException(AuthErrorCode.CANNOT_FIND_AT);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getUserList(HttpServletRequest httpServletRequest, @RequestBody String userId) {
        String accessToken = CookieService.extractAccessTokenFromCookie(httpServletRequest);
        userViewService.viewUserList(accessToken, userId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
    

}
