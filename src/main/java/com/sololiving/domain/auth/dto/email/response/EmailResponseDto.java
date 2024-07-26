package com.sololiving.domain.auth.dto.email.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class EmailResponseDto {
    private String to;
    private String subject;
    private String message;
}
