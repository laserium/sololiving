package com.sololiving.domain.comment.exception;

import com.sololiving.global.exception.error.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommentErrorCode implements ErrorCode {

    NOT_FOUND_COMMENT("COMMENT_E001", "댓글을 찾을 수 없음");

    private final String code;
    private final String message;

}
