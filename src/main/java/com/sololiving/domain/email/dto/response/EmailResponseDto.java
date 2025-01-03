package com.sololiving.domain.email.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class EmailResponseDto {
    private String to;
    private String subject;
    private String message;
}
