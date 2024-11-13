package com.sololiving.domain.alarm.exception;

import com.sololiving.global.exception.success.SuccessCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlarmSuccessCode implements SuccessCode {

    SUCCESS_TO_POST_ARTICLE("ALARM_S001", "게시글 작성 성공");

    private final String code;
    private final String message;

}
