package com.sololiving.domain.article.exception;

import com.sololiving.global.exception.success.SuccessCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ArticleSuccessCode implements SuccessCode {

    SUCCESS_TO_POST_ARTICLE("ARTICLE_S001", "게시글 작성 성공"),
    SUCCESS_TO_UPDATE_ARTICLE("ARTICLE_S002", "게시글 수정 성공"),
    SUCCESS_TO_DELETE_ARTICLE("ARTICLE_S003", "게시글 삭제 성공"),
    SUCCESS_TO_LIKE_ARTICLE("ARTICLE_S004", "게시글 추천 성공"),
    SUCCESS_TO_CANCLE_LIKE_ARTICLE("ARTICLE_S005", "게시글 추천 취소 성공"),
    SUCCESS_TO_UPDATE_STATUS("ARTICLE_S006", "게시글 상태 변경 성공");

    private final String code;
    private final String message;

}
