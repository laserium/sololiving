package com.sololiving.global.exception;

import com.sololiving.global.exception.error.ErrorCode;
import com.sololiving.global.exception.error.ErrorResponse;
import com.sololiving.global.exception.success.SuccessCode;
import com.sololiving.global.exception.success.SuccessResponse;

public class ResponseMessage {

    public static SuccessResponse createSuccessResponse(SuccessCode successCode) {
        return SuccessResponse.builder()
                .code(successCode.getCode())
                .message(successCode.getMessage())
                .build();
    }

    public static ErrorResponse createErrorResponse(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
    }

}
