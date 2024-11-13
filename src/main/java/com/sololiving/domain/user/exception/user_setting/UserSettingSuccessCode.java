package com.sololiving.domain.user.exception.user_setting;

import com.sololiving.global.exception.success.SuccessCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserSettingSuccessCode implements SuccessCode {

    PUSH_NOTIFICATION_ON("USER_S001", "푸쉬 알림 설정 켜짐(ON)"),
    PUSH_NOTIFICATION_OFF("USER_S001", "푸쉬 알림 설정 꺼짐(OFF)"),
    PROFILE_SHARING_ON("USER_S002", "프로필 공유 여부 설정 켜짐(ON)"),
    PROFILE_SHARING_OFF("USER_S003", "프로필 공유 여부 설정 꺼짐(OFF)"),
    ARTICLE_SHARING_ON("USER_S004", "작성한 게시글 공유 여부 설정 켜짐(ON)"),
    ARTICLE_SHARING_OFF("USER_S005", "작성한 게시글 공유 여부 설정 꺼짐(OFF)"),
    COMMENT_SHARING_ON("USER_S006", "작성한 댓글 공유 여부 설정 켜짐(ON)"),
    COMMENT_SHARING_OFF("USER_S007", "작성한 댓글 공유 여부 설정 꺼짐(OFF)"),
    LIKED_SHARING_ON("USER_S008", "추천한 게시글 공유 여부 설정 켜짐(ON)"),
    LIKED_SHARING_OFF("USER_S009", "추천한 게시글 공유 여부 설정 꺼짐(OFF)");

    private final String code;
    private final String message;

}
