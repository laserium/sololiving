package com.sololiving.domain.report.exception;

import com.sololiving.global.exception.error.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReportErrorCode implements ErrorCode {

    FAIL_TO_UPLOAD_FILE("MEDIA_E001", "미디어 파일 업로드 실패");

    private final String code;
    private final String message;

}
