package com.sololiving.domain.alarm.vo;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

@Getter
public class AlarmVo {

    private Long alarmId; // PK
    private int alarmTypeId; // FK => ALARM_TYPE
    private String userId; // 알림을 받을 사용자 ID
    private String actorId; // 알림을 발생시킨 사용자 ID (optional, 알림을 트리거한 사용자)
    private Long articleId; // 알림이 발생한 게시글 ID (optional)
    private Long commentId; // 알림이 발생한 댓글 ID (optional)
    private boolean isRead; // 읽음 여부 (default : false)
    private LocalDateTime readAt; // 알림 읽음 시간 (NULL이면 읽지 않은 상태)

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

}
