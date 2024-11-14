package com.sololiving.domain.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class SignUpVerificationSmsRequestDto {

    @Getter
    @NoArgsConstructor
    public static class SendSignUpVerificationSmsRequestDto {
        private String contact;
    }

    @Getter
    @NoArgsConstructor
    public static class CheckSignUpVerificationSmsRequestDto {
        private String contact;
        private String code;
    }

}
