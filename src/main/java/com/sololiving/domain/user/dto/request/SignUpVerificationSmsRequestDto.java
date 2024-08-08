package com.sololiving.domain.user.dto.request;

import lombok.Builder;
import lombok.Getter;

public class SignUpVerificationSmsRequestDto {

    @Getter
    @Builder
    public static class SendSignUpVerificationSmsRequestDto {
        private String contact;
    }

    @Getter
    @Builder
    public static class CheckSignUpVerificationSmsRequestDto {
        private String contact;
        private String code;
    }

}
