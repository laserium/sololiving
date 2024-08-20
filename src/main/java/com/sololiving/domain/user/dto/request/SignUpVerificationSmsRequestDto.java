package com.sololiving.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SignUpVerificationSmsRequestDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SendSignUpVerificationSmsRequestDto {
        private String contact;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CheckSignUpVerificationSmsRequestDto {
        private String contact;
        private String code;
    }

}
