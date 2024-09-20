package com.sololiving.domain.media.exception;

import com.sololiving.global.exception.success.SuccessCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MediaSuccessCode implements SuccessCode {

    SUCCESS_TO_UPLOAD_FILE("MEDIA_S001", "미디어 파일 업로드 성공");

    private final String code;
    private final String message;

}
