package com.sololiving.domain.email.vo;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailVerificationTokenVo {

    private Long id;
    private String token;
    private String userId;
    private String newEmail;
    private LocalDateTime expiresIn;

}
