package com.sololiving.domain.comment.exception;

import com.sololiving.global.exception.error.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommentErrorCode implements ErrorCode {

    NOT_FOUND_COMMENT("COMMENT_E001", "댓글을 찾을 수 없음"),
    CANNOT_LIKE_MY_COMMENT("COMMENT_E002", "본인이 작성한 댓글에 추천 불가"),
    CANNOT_LIKE_DUPLICATE("COMMENT_E003", "이미 추천한 댓글에 재추천 불가"),
    NOT_LIKED_COMMENT("COMMENT_E004", "아직 추천하지 않은 댓글입니다"),
    CANNOT_DISLIKE_MY_COMMENT("COMMENT_E004", "본인이 작성한 댓글 추천 취소 불가");

    private final String code;
    private final String message;

}
