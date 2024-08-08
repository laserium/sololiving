package com.sololiving.domain.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.user.dto.request.SignUpVerificationSmsRequestDto.CheckSignUpVerificationSmsRequestDto;
import com.sololiving.domain.user.dto.request.SignUpVerificationSmsRequestDto.SendSignUpVerificationSmsRequestDto;
import com.sololiving.domain.user.service.UserAuthService;
import com.sololiving.global.exception.ResponseMessage;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.exception.success.SuccessResponse;
import com.sololiving.global.security.sms.exception.SmsErrorCode;
import com.sololiving.global.security.sms.exception.SmsSuccessCode;
import com.sololiving.global.security.sms.service.SmsService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

}
