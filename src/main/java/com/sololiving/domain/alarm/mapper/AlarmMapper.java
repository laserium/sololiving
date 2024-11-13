package com.sololiving.domain.alarm.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.sololiving.domain.alarm.dto.request.CommentAlarmRequestDto;
import com.sololiving.domain.alarm.dto.request.ReCommentAlarmRequestDto;

@Mapper
public interface AlarmMapper {

    // 게시글에 댓글 작성될 경우 댓글 알림 생성
    void insertCommentAlarm(CommentAlarmRequestDto commentAlarmRequestDto);

    // 댓글에 대댓글 작성될 경우 대댓글 알림 생성
    void insertReCommentAlarm(ReCommentAlarmRequestDto reCommentAlarmRequestDto);
}
