package com.sololiving.domain.media.exception;

import com.sololiving.global.exception.error.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MediaErrorCode implements ErrorCode {

    FAIL_TO_UPLOAD_FILE("MEDIA_E001", "미디어 파일 업로드 실패"),
    FAIL_TO_CONVERT_MULTIPARTFILE_TO_FILE("MEDIA_E002", "파일 변환 실패"),
    UNSUPPORTED_FORMAT("MEDIA_E003", "지원하지 않는 파일 형식입니다."),
    FILE_SIZE_EXCEEDS_LIMIT("MEDIA_E004", "파일 크기는 20MB를 초과할 수 없습니다.");

    private final String code;
    private final String message;

}
