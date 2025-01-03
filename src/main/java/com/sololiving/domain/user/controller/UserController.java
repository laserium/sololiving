package com.sololiving.domain.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.auth.dto.auth.request.SignUpRequestDto;
import com.sololiving.domain.user.dto.request.UpdateUserRequestICDto.UpdateUserBirthRequestDto;
import com.sololiving.domain.user.dto.request.UpdateUserRequestICDto.UpdateUserAddressRequestDto;
import com.sololiving.domain.user.dto.request.UpdateUserRequestICDto.UpdateUserContactRequestDto;
import com.sololiving.domain.user.dto.request.UpdateUserRequestICDto.UpdateUserEmailRequestDto;
import com.sololiving.domain.user.dto.request.UpdateUserRequestICDto.UpdateUserGenderRequestDto;
import com.sololiving.domain.user.dto.request.UpdateUserRequestICDto.UpdateUserNicknameRequestDto;
import com.sololiving.domain.user.dto.request.UpdateUserRequestICDto.UpdateUserPasswordRequestDto;
import com.sololiving.domain.user.dto.request.UpdateUserRequestICDto.ValidateUpdateUserContactRequestDto;
import com.sololiving.domain.user.dto.response.ValidateUserContactResponseDto;
import com.sololiving.domain.user.enums.Status;
import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.domain.user.exception.UserSuccessCode;
import com.sololiving.domain.user.service.UserService;
import com.sololiving.global.aop.admin.AdminOnly;
import com.sololiving.global.exception.GlobalErrorCode;
import com.sololiving.global.exception.ResponseMessage;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.exception.success.SuccessResponse;
import com.sololiving.global.util.SecurityUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

        private final UserService userService;

        // 회원가입
        @PostMapping("/signup")
        public ResponseEntity<SuccessResponse> createUser(@RequestBody SignUpRequestDto requestDto,
                        HttpServletRequest httpServletRequest) {
                userService.signUp(requestDto, httpServletRequest);

                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(ResponseMessage.createSuccessResponse(UserSuccessCode.SIGN_UP_SUCCESS));
        }

        // 회원탈퇴
        @DeleteMapping("")
        public ResponseEntity<SuccessResponse> deleteUser(HttpServletRequest httpServletRequest) {
                String userId = SecurityUtil.getCurrentUserId();
                userService.withdraw(userId, httpServletRequest);
                return ResponseEntity.status(HttpStatus.OK)
                                .body(ResponseMessage
                                                .createSuccessResponse(UserSuccessCode.USER_DELETE_SUCCESS));
        }

        // 상태변경 => 수정 예정
        @AdminOnly
        @PatchMapping("/status/{status}")
        public ResponseEntity<?> updateUserStatus(@PathVariable Status status,
                        HttpServletRequest httpServletRequest) {
                String userId = SecurityUtil.getCurrentUserId();
                userService.updateStatus(userId, status);
                if (status == Status.ACTIVE) {
                        return ResponseEntity.status(HttpStatus.OK)
                                        .body(ResponseMessage
                                                        .createSuccessResponse(UserSuccessCode.USER_STATUS_ACTIVE));
                }
                if (status == Status.BLOCKED) {
                        return ResponseEntity.status(HttpStatus.OK)
                                        .body(ResponseMessage
                                                        .createSuccessResponse(UserSuccessCode.USER_STATUS_BLOCKED));
                }
                if (status == Status.WITHDRAWN) {
                        return ResponseEntity.status(HttpStatus.OK)
                                        .body(ResponseMessage
                                                        .createSuccessResponse(UserSuccessCode.USER_STATUS_WITHDRAWN));
                }

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(UserErrorCode.NO_USER_STATUS_REQUEST);
        }

        // 회원 이메일 변경
        @PatchMapping("/email")
        public ResponseEntity<SuccessResponse> updateUserEmail(
                        @RequestBody UpdateUserEmailRequestDto requestDto,
                        HttpServletRequest httpServletRequest) {
                String userId = SecurityUtil.getCurrentUserId();
                userService.sendUpdateNewEmailRequest(userId, requestDto);
                return ResponseEntity.status(HttpStatus.OK)
                                .body(ResponseMessage
                                                .createSuccessResponse(UserSuccessCode.UPDATE_EMAIL_REQUEST_SUCCESS));
        }

        // 회원 연락처 변경 전 인증 문자 전송
        @PostMapping("/contact/sms-verification")
        public ResponseEntity<ValidateUserContactResponseDto> validateUpdateUserContact(
                        @RequestBody ValidateUpdateUserContactRequestDto requestDto,
                        HttpServletRequest httpServletRequest) {
                String userId = SecurityUtil.getCurrentUserId();
                if (requestDto.getContact() == null) {
                        throw new ErrorException(GlobalErrorCode.REQUEST_IS_NULL);
                }

                return ResponseEntity.status(HttpStatus.OK)
                                .body(userService.checkUpdateUserContact(userId, requestDto.getContact()));
        }

        // 회원 연락처 변경
        @PatchMapping("/contact")
        public ResponseEntity<SuccessResponse> updateUserContact(@RequestBody UpdateUserContactRequestDto requestDto,
                        HttpServletRequest httpServletRequest) {
                String userId = SecurityUtil.getCurrentUserId();
                userService.updateUserContact(userId, requestDto);
                return ResponseEntity.status(HttpStatus.OK)
                                .body(ResponseMessage
                                                .createSuccessResponse(UserSuccessCode.UPDATE_USER_REQUEST_SUCCESS));
        }

        // 회원 비밀번호 변경
        @PatchMapping("/password")
        public ResponseEntity<SuccessResponse> updateUserPassword(
                        @RequestBody UpdateUserPasswordRequestDto requestDto,
                        HttpServletRequest httpServletRequest) {
                String userId = SecurityUtil.getCurrentUserId();
                userService.updateUserPassword(userId, requestDto);
                return ResponseEntity.status(HttpStatus.OK)
                                .body(ResponseMessage
                                                .createSuccessResponse(UserSuccessCode.UPDATE_USER_REQUEST_SUCCESS));
        }

        // 회원 닉네임 변경
        @PatchMapping("/nickname")
        public ResponseEntity<SuccessResponse> updateUserNickname(
                        @RequestBody UpdateUserNicknameRequestDto requestDto,
                        HttpServletRequest httpServletRequest) {
                String userId = SecurityUtil.getCurrentUserId();
                userService.updateUserNickname(userId, requestDto);
                return ResponseEntity.status(HttpStatus.OK)
                                .body(ResponseMessage
                                                .createSuccessResponse(UserSuccessCode.UPDATE_USER_REQUEST_SUCCESS));
        }

        // 회원 성별 변경
        @PatchMapping("/gender")
        public ResponseEntity<SuccessResponse> updateUserGender(
                        @RequestBody UpdateUserGenderRequestDto requestDto,
                        HttpServletRequest httpServletRequest) {
                String userId = SecurityUtil.getCurrentUserId();
                userService.updateUserGender(userId, requestDto);
                return ResponseEntity.status(HttpStatus.OK)
                                .body(ResponseMessage
                                                .createSuccessResponse(UserSuccessCode.UPDATE_USER_REQUEST_SUCCESS));
        }

        // 회원 주소 변경
        @PatchMapping("/address")
        public ResponseEntity<SuccessResponse> updateUserAddress(
                        @RequestBody UpdateUserAddressRequestDto requestDto,
                        HttpServletRequest httpServletRequest) {
                String userId = SecurityUtil.getCurrentUserId();
                userService.updateUserAddress(userId, requestDto);
                return ResponseEntity.status(HttpStatus.OK)
                                .body(ResponseMessage
                                                .createSuccessResponse(UserSuccessCode.UPDATE_USER_REQUEST_SUCCESS));
        }

        // 회원 생일 변경
        @PatchMapping("/birth")
        public ResponseEntity<SuccessResponse> updateUserBirth(
                        @RequestBody UpdateUserBirthRequestDto requestDto,
                        HttpServletRequest httpServletRequest) {
                String userId = SecurityUtil.getCurrentUserId();
                userService.updateUserBirth(userId, requestDto);
                return ResponseEntity.status(HttpStatus.OK)
                                .body(ResponseMessage
                                                .createSuccessResponse(UserSuccessCode.UPDATE_USER_REQUEST_SUCCESS));
        }

}
