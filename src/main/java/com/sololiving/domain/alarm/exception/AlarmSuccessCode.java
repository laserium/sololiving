package com.sololiving.domain.alarm.exception;

import com.sololiving.global.exception.success.SuccessCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlarmSuccessCode implements SuccessCode {

    SUCCESS_TO_READ_ALARM("ALARM_S001", "알림 읽음 처리 성공"),
    SUCCESS_TO_DELETE_ALARM("ALARM_S002", "알림 삭제 성공");

    private final String code;
    private final String message;

}
