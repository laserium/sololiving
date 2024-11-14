package com.sololiving.domain.report.exception;

import com.sololiving.global.exception.success.SuccessCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReportSuccessCode implements SuccessCode {

    REPORT_SUCCESS("REPORT_S001", "신고 완료"),
    SUCCESS_TO_DELETE_REPORT("REPORT_S002", "신고 기록 삭제 완료");

    private final String code;
    private final String message;

}
