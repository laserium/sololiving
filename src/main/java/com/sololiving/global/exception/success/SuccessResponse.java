package com.sololiving.global.exception.success;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SuccessResponse {
    private String code;
    private String message;
}
