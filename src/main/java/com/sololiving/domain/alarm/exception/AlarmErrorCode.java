package com.sololiving.domain.alarm.exception;

import com.sololiving.global.exception.error.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlarmErrorCode implements ErrorCode {

    FAIL_TO_UPLOAD_FILE("ALARM_E001", "미디어 파일 업로드 실패");

    private final String code;
    private final String message;

}
