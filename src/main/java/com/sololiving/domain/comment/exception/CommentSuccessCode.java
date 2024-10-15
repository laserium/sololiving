package com.sololiving.domain.comment.exception;

import com.sololiving.global.exception.success.SuccessCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommentSuccessCode implements SuccessCode {

    SUCCESS_TO_POST_COMMENT("COMMENT_S001", "댓글 작성 성공");

    private final String code;
    private final String message;

}
