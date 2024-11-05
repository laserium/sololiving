package com.sololiving.domain.comment.exception;

import com.sololiving.global.exception.success.SuccessCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommentSuccessCode implements SuccessCode {

    SUCCESS_TO_POST_COMMENT("COMMENT_S001", "댓글 작성 성공"),
    SUCCESS_TO_DELETE_COMMENT("COMMENT_S002", "댓글 삭제 성공"),
    SUCCESS_TO_UPDATE_COMMENT("COMMENT_S003", "댓글 수정 성공"),
    SUCCESS_TO_LIKE_COMMENT("COMMENT_S004", "댓글 추천 성공");

    private final String code;
    private final String message;

}
