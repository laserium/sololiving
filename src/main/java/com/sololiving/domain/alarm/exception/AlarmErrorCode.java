package com.sololiving.domain.alarm.exception;

import com.sololiving.global.exception.error.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlarmErrorCode implements ErrorCode {

    ALARM_NOT_FOUND("ALARM_E001", "알림 정보 없음"),
    NO_PERMISSION_TO_VIEW_ALARM("ALARM_E002", "알림 조회 권한이 없습니다");

    private final String code;
    private final String message;

}
