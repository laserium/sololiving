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
import com.sololiving.domain.auth.dto.oauth.response.naver.NaverUserInfoResponseDto.Response;
import com.sololiving.domain.user.dto.request.UpdateUsersEmailRequestDto;
import com.sololiving.domain.user.dto.request.UpdateUsersNicknameRequestDto;
import com.sololiving.domain.user.enums.Status;
import com.sololiving.domain.user.exception.UserSuccessCode;
import com.sololiving.domain.user.service.UserService;
import com.sololiving.global.exception.ResponseMessage;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.exception.success.SuccessResponse;
import com.sololiving.global.security.jwt.exception.TokenErrorCode;
import com.sololiving.global.util.CookieService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final CookieService cookieService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<SuccessResponse> createUser(@RequestBody SignUpRequestDto signUpRequestDto) {
        userService.signUp(signUpRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseMessage.createSuccessResponse(UserSuccessCode.SIGN_UP_SUCCESS));
    }

    // 회원탈퇴
    @DeleteMapping("")
    public ResponseEntity<SuccessResponse> deleteUser(HttpServletRequest httpServletRequest) {
        String accessToken = cookieService.extractAccessTokenFromCookie(httpServletRequest);
        if (accessToken != null) {
            userService.deleteUserRequest(accessToken);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseMessage.createSuccessResponse(UserSuccessCode.USER_DELETE_SUCCESS));
        } else
            throw new ErrorException(TokenErrorCode.CANNOT_FIND_AT);
    }

    // 상태변경
    @PatchMapping("/status/{status}")
    public ResponseEntity<?> updateUserStatus(@PathVariable Status status, HttpServletRequest httpServletRequest) {
        String accessToken = cookieService.extractAccessTokenFromCookie(httpServletRequest);
        userService.updateStatus(accessToken, status);
        if (status == Status.ACTIVE) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseMessage.createSuccessResponse(UserSuccessCode.USER_STATUS_ACTIVE));
        } else if (status == Status.BLOCKED) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseMessage.createSuccessResponse(UserSuccessCode.USER_STATUS_BLOCKED));
        } else
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseMessage.createSuccessResponse(UserSuccessCode.USER_STATUS_WITHDRAWN));
    }

    // 유저 이메일 변경
    @PatchMapping("/email")
    public ResponseEntity<?> updateUserEmail(@RequestBody UpdateUsersEmailRequestDto patchUsersEmailRequestDto,
            HttpServletRequest httpServletRequest) {
        String accessToken = cookieService.extractAccessTokenFromCookie(httpServletRequest);
        userService.sendUpdateNewEmailRequest(accessToken, patchUsersEmailRequestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage.createSuccessResponse(UserSuccessCode.UPDATE_EMAIL_REQUEST_SUCCESS));
    }

    // 유저 닉네임 변경
    @PatchMapping("/nickname")
    public ResponseEntity<?> updateUserNickname(
            @RequestBody UpdateUsersNicknameRequestDto updateUsersNicknameRequestDto,
            HttpServletRequest httpServletRequest) {
        String accessToken = cookieService.extractAccessTokenFromCookie(httpServletRequest);
        userService.updateUserNicknameRequest(accessToken, updateUsersNicknameRequestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage.createSuccessResponse(UserSuccessCode.UPDATE_USER_NICKNAME_SUCCESS));
    }
}
