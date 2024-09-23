package com.sololiving.domain.article.exception;

import com.sololiving.global.exception.error.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ArticleErrorCode implements ErrorCode {

    FAIL_TO_UPLOAD_FILE("ARTICLE_E001", "미디어 파일 업로드 실패"),
    ARTICLE_NOT_FOUND("ARTICLE_E002", "존재하지 않는 게시글");

    private final String code;
    private final String message;

}
