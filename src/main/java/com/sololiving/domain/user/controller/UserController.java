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
import com.sololiving.domain.user.dto.request.UpdateUserAddressRequestDto;
import com.sololiving.domain.user.dto.request.UpdateUserBirthRequestDto;
import com.sololiving.domain.user.dto.request.UpdateUserEmailRequestDto;
import com.sololiving.domain.user.dto.request.UpdateUserGenderRequestDto;
import com.sololiving.domain.user.dto.request.UpdateUserNicknameRequestDto;
import com.sololiving.domain.user.dto.request.UpdateUserPasswordRequestDto;
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
    public ResponseEntity<SuccessResponse> updateUserStatus(@PathVariable Status status,
            HttpServletRequest httpServletRequest) {
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
    public ResponseEntity<SuccessResponse> updateUserEmail(
            @RequestBody UpdateUserEmailRequestDto patchUsersEmailRequestDto,
            HttpServletRequest httpServletRequest) {
        String accessToken = cookieService.extractAccessTokenFromCookie(httpServletRequest);
        userService.sendUpdateNewEmailRequest(accessToken, patchUsersEmailRequestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage.createSuccessResponse(UserSuccessCode.UPDATE_EMAIL_REQUEST_SUCCESS));
    }

    // 유저 닉네임 변경
    @PatchMapping("/nickname")
    public ResponseEntity<SuccessResponse> updateUserNickname(
            @RequestBody UpdateUserNicknameRequestDto updateUsersNicknameRequestDto,
            HttpServletRequest httpServletRequest) {
        String accessToken = cookieService.extractAccessTokenFromCookie(httpServletRequest);
        userService.updateUserNickname(accessToken, updateUsersNicknameRequestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage.createSuccessResponse(UserSuccessCode.UPDATE_USER_REQUEST_SUCCESS));
    }

    // 유저 성별 변경
    @PatchMapping("/gender")
    public ResponseEntity<SuccessResponse> updateUserGender(
            @RequestBody UpdateUserGenderRequestDto updateUserGenderRequestDto,
            HttpServletRequest httpServletRequest) {
        String accessToken = cookieService.extractAccessTokenFromCookie(httpServletRequest);
        userService.updateUserGender(accessToken, updateUserGenderRequestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage.createSuccessResponse(UserSuccessCode.UPDATE_USER_REQUEST_SUCCESS));
    }

    // 유저 주소 변경
    @PatchMapping("/address")
    public ResponseEntity<SuccessResponse> updateUserAddress(
            @RequestBody UpdateUserAddressRequestDto updateUserAddressRequestDto,
            HttpServletRequest httpServletRequest) {
        String accessToken = cookieService.extractAccessTokenFromCookie(httpServletRequest);
        userService.updateUserAddress(accessToken, updateUserAddressRequestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage.createSuccessResponse(UserSuccessCode.UPDATE_USER_REQUEST_SUCCESS));
    }

    // 유저 주소 변경
    @PatchMapping("/birth")
    public ResponseEntity<SuccessResponse> updateUserBirth(
            @RequestBody UpdateUserBirthRequestDto updateUserBirthRequestDto,
            HttpServletRequest httpServletRequest) {
        String accessToken = cookieService.extractAccessTokenFromCookie(httpServletRequest);
        userService.updateUserBirth(accessToken, updateUserBirthRequestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage.createSuccessResponse(UserSuccessCode.UPDATE_USER_REQUEST_SUCCESS));
    }

    // 유저 비밀번호 변경
    @PatchMapping("/password")
    public ResponseEntity<SuccessResponse> updateUserPassword(
            @RequestBody UpdateUserPasswordRequestDto updateUserPasswordRequestDto,
            HttpServletRequest httpServletRequest) {
        String accessToken = cookieService.extractAccessTokenFromCookie(httpServletRequest);
        userService.updateUserPassword(accessToken, updateUserPasswordRequestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage.createSuccessResponse(UserSuccessCode.UPDATE_USER_REQUEST_SUCCESS));
    }
}
