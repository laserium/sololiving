package com.sololiving.domain.article.exception;

import com.sololiving.global.exception.success.SuccessCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ArticleSuccessCode implements SuccessCode {

    SUCCESS_TO_POST_ARTICLE("ARTICLE_S001", "게시글 작성 성공"),
    SUCCESS_TO_UPDATE_ARTICLE("ARTICLE_S002", "게시글 수정 성공"),
    SUCCESS_TO_DELETE_ARTICLE("ARTICLE_S003", "게시글 삭제 성공");

    private final String code;
    private final String message;

}
