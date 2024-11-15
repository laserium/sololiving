package com.sololiving.domain.log.exception;

import com.sololiving.global.exception.success.SuccessCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LogSuccessCode implements SuccessCode {

    SUCCESS_TO_POST_ARTICLE("ARTICLE_S001", "게시글 작성 성공");

    private final String code;
    private final String message;

}
