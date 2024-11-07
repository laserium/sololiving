package com.sololiving.global.exception;

import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.auth.exception.auth.AuthErrorCode;
import com.sololiving.domain.email.exception.EmailErrorCode;
import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.global.exception.error.ErrorResponse;
import com.sololiving.global.security.jwt.exception.TokenErrorCode;
import com.sololiving.global.security.sms.exception.SmsErrorCode;

import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping(value = "/docs/error-codes", produces = "application/json; charset=UTF-8")
public class ExceptionErrorController {

    @GetMapping("/user")
    public List<ErrorResponse> getUserErrorCodes() {
        return Arrays.stream(UserErrorCode.values())
                .map(errorCode -> ErrorResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build())
                .collect(Collectors.toList());
    }

    @GetMapping("/auth")
    public List<ErrorResponse> getAuthErrorCodes() {
        return Arrays.stream(AuthErrorCode.values())
                .map(errorCode -> ErrorResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build())
                .collect(Collectors.toList());
    }

    @GetMapping("/token")
    public List<ErrorResponse> getTokenErrorCode() {
        return Arrays.stream(TokenErrorCode.values())
                .map(errorCode -> ErrorResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build())
                .collect(Collectors.toList());
    }

    @GetMapping("/email")
    public List<ErrorResponse> getEmailErrorCode() {
        return Arrays.stream(EmailErrorCode.values())
                .map(errorCode -> ErrorResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build())
                .collect(Collectors.toList());
    }

    @GetMapping("/sms")
    public List<ErrorResponse> getSmsErrorCode() {
        return Arrays.stream(SmsErrorCode.values())
                .map(errorCode -> ErrorResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build())
                .collect(Collectors.toList());
    }

}
