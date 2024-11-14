package com.sololiving.domain.report.exception;

import com.sololiving.global.exception.error.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReportErrorCode implements ErrorCode {

    REPORT_NOT_FOUND("REPORT_E001", "해당 신고 기록 없음"),
    REPORT_INPUT_INVALID("REPORT_E002", "상태 입력 값이 올바르지 않습니다"),
    REPORT_ALREADY_UPDATED("REPORT_E003", "신고가 이미 처리되었거나 거절된 상태입니다");

    private final String code;
    private final String message;

}
