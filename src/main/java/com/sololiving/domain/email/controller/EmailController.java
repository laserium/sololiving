package com.sololiving.domain.email.controller;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sololiving.domain.email.service.EmailVerificationService;
import com.sololiving.domain.email.vo.EmailVerificationTokenVo;
import com.sololiving.domain.user.service.UserService;
import com.sololiving.global.exception.success.SuccessResponse;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {

    private final UserService userService;
    private final EmailVerificationService emailVerificationService;

    @GetMapping("/confirmation/{token}")
    @ResponseBody
    public ResponseEntity<SuccessResponse> confirmEmail(@PathVariable("token") String token) {
        EmailVerificationTokenVo verificationToken = emailVerificationService.getVerificationToken(token);
        userService.updateUserEmail(verificationToken);
        emailVerificationService.deleteVerificationToken(verificationToken);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("/email/confirmation-success"))
                .build();
    }

    @GetMapping("/confirmation-success")
    public String confirmationSuccess() {
        return "confirmation-success";
    }
}
