// package com.sololiving.global.exception;

// import org.springframework.web.bind.annotation.RestController;

// import com.sololiving.domain.auth.exception.auth.AuthSuccessCode;
// import com.sololiving.domain.email.exception.EmailSuccessCode;
// import com.sololiving.domain.user.exception.UserSuccessCode;
// import com.sololiving.global.exception.success.SuccessResponse;
// import com.sololiving.global.security.sms.exception.SmsSuccessCode;

// import java.util.List;
// import java.util.Arrays;
// import java.util.stream.Collectors;

// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;

// @RestController
// @RequestMapping(value = "/docs/success-codes", produces = "application/json;
// charset=UTF-8")
// public class ExceptionSuccessController {

// @GetMapping("/user")
// public List<SuccessResponse> getUserErrorCodes() {
// return Arrays.stream(UserSuccessCode.values())
// .map(successCode -> SuccessResponse.builder()
// .code(successCode.getCode())
// .message(successCode.getMessage())
// .build())
// .collect(Collectors.toList());
// }

// @GetMapping("/auth")
// public List<SuccessResponse> getAuthErrorCodes() {
// return Arrays.stream(AuthSuccessCode.values())
// .map(successCode -> SuccessResponse.builder()
// .code(successCode.getCode())
// .message(successCode.getMessage())
// .build())
// .collect(Collectors.toList());
// }

// // @GetMapping("/token")
// // public List<SuccessResponse> getTokenErrorCode() {
// // return Arrays.stream(TokenSuccessCode.values())
// // .map(successCode -> SuccessResponse.builder()
// // .code(successCode.getCode())
// // .message(successCode.getMessage())
// // .build())
// // .collect(Collectors.toList());
// // }

// @GetMapping("/email")
// public List<SuccessResponse> getEmailErrorCode() {
// return Arrays.stream(EmailSuccessCode.values())
// .map(successCode -> SuccessResponse.builder()
// .code(successCode.getCode())
// .message(successCode.getMessage())
// .build())
// .collect(Collectors.toList());
// }

// @GetMapping("/sms")
// public List<SuccessResponse> getSmsErrorCode() {
// return Arrays.stream(SmsSuccessCode.values())
// .map(successCode -> SuccessResponse.builder()
// .code(successCode.getCode())
// .message(successCode.getMessage())
// .build())
// .collect(Collectors.toList());
// }

// }
