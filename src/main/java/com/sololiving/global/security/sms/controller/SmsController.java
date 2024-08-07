package com.sololiving.global.security.sms.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.global.exception.ResponseMessage;
import com.sololiving.global.security.sms.dto.request.SmsCheckRequestDto;
import com.sololiving.global.security.sms.dto.request.SmsSendRequestDto;
import com.sololiving.global.security.sms.exception.SmsErrorCode;
import com.sololiving.global.security.sms.exception.SmsSuccessCode;
import com.sololiving.global.security.sms.service.SmsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sms")
public class SmsController {

    private final SmsService smsService;

    // 인증번호 발송
    @PostMapping("/send")
    public ResponseEntity<?> sendSms(@RequestBody SmsSendRequestDto smsSendRequestDto) {
        smsService.sendSms(smsSendRequestDto.getContact());
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage.createSuccessResponse(SmsSuccessCode.SUCCESS_TO_SEND));
    }

    // 인증번호 확인
    @PostMapping("/check")
    public ResponseEntity<?> checkSms(@RequestBody SmsCheckRequestDto smsCheckRequestDto) {
        boolean isCorrect = smsService.checkSms(smsCheckRequestDto.getContact(), smsCheckRequestDto.getCode());
        if (isCorrect) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseMessage.createSuccessResponse(SmsSuccessCode.CERTIFICATION_NUMBER_CORRECT));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseMessage.createErrorResponse(SmsErrorCode.CERTIFICATION_NUMBER_INCORRECT));
        }
    }
}
