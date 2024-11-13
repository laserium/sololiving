package com.sololiving.domain.user.exception.user_setting;

import com.sololiving.global.exception.error.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserSettingErrorCode implements ErrorCode {
    INVALID_SETTING_TYPE("USST_E001", "설정 명칭이 유효하지 않습니다."),
    ARTICLE_SHARING_DISABLED("USST_E002", "해당 사용자의 작성한 게시글 조회 설정이 꺼져있습니다."),
    COMMENT_SHARING_DISABLED("USST_E003", "해당 사용자의 작성한 댓글 조회 설정이 꺼져있습니다."),
    LIKED_SHARING_DISABLED("USST_E004", "해당 사용자의 추천한 게시글 조회 설정이 꺼져있습니다.");

    private final String code;
    private final String message;

}
