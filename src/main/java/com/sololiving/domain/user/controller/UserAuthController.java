package com.sololiving.domain.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.auth.dto.auth.request.VerifyPasswordRequestDto;
import com.sololiving.domain.user.dto.request.SignUpVerificationSmsRequestDto.CheckSignUpVerificationSmsRequestDto;
import com.sololiving.domain.user.dto.request.SignUpVerificationSmsRequestDto.SendSignUpVerificationSmsRequestDto;
import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.domain.user.exception.UserSuccessCode;
import com.sololiving.domain.user.service.UserAuthService;
import com.sololiving.global.exception.ResponseMessage;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.exception.success.SuccessResponse;
import com.sololiving.global.security.sms.exception.SmsErrorCode;
import com.sololiving.global.security.sms.exception.SmsSuccessCode;
import com.sololiving.global.security.sms.service.SmsService;
import com.sololiving.global.util.SecurityUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/auth")
public class UserAuthController {

    private final UserAuthService userAuthService;
    private final SmsService smsService;

    // 회원가입 시 휴대폰 인증번호 전송
    @PostMapping("/contact-verification/send")
    public ResponseEntity<SuccessResponse> sendSignUpVerificationSms(
            @RequestBody SendSignUpVerificationSmsRequestDto requestDto) {
        userAuthService.sendSignUpVerificationSms(requestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage.createSuccessResponse(SmsSuccessCode.SUCCESS_TO_SEND));
    }

    // 회원가입 시 휴대폰 인증번호 검증
    @PostMapping("/contact-verification/check")
    public ResponseEntity<SuccessResponse> checkSignUpVerificationCode(
            @RequestBody CheckSignUpVerificationSmsRequestDto requestDto) {
        if (smsService.checkSms(requestDto.getContact(), requestDto.getCode())) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseMessage.createSuccessResponse(SmsSuccessCode.CERTIFICATION_NUMBER_CORRECT));
        } else
            throw new ErrorException(SmsErrorCode.CERTIFICATION_NUMBER_INCORRECT);
    }

    // 회원가입 시 아이디 중복 확인 검증
    @GetMapping("/id-duplicate-verification/{userId}")
    public ResponseEntity<SuccessResponse> getIdDuplicateVerification(@PathVariable String userId) {
        if (userId == null) {
            throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
        }
        if (userAuthService.isUserIdAvailable(userId)) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseMessage.createSuccessResponse(UserSuccessCode.USER_ID_AVAILABLE));
        } else
            throw new ErrorException(UserErrorCode.ID_ALREADY_EXISTS);
    }

    // 비밀번호 검증
    @PostMapping("/password-verification")
    public ResponseEntity<?> verifyUserPassword(@RequestBody VerifyPasswordRequestDto requestDto,
            HttpServletRequest httpServletRequest) {
        String userId = SecurityUtil.getCurrentUserId();
        if (userAuthService.verifyUserPassword(requestDto.getUserPwd(), userId)) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseMessage.createSuccessResponse(UserSuccessCode.USER_PASSWORD_CORRECT));
        } else
            throw new ErrorException(UserErrorCode.USER_PASSWORD_INCORRECT);
    }
}
