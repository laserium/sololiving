package com.sololiving.domain.alarm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sololiving.domain.alarm.dto.request.CommentAlarmRequestDto;
import com.sololiving.domain.alarm.dto.request.ReCommentAlarmRequestDto;
import com.sololiving.domain.alarm.mapper.AlarmMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmMapper alarmMapper;

    // 게시글에 댓글 작성될 경우 댓글 알림 생성
    @Transactional
    public void addCommentAlarm(String articleWriter, String commentWriter, Long articleId, Long commentId) {

        CommentAlarmRequestDto commentAlarmRequestDto = CommentAlarmRequestDto.builder()
                .articleWriter(articleWriter)
                .commentWriter(commentWriter)
                .articleId(articleId)
                .commentId(commentId)
                .build();
        alarmMapper.insertCommentAlarm(commentAlarmRequestDto);
    }

    // 댓글에 대댓글 작성될 경우 대댓글 알림 생성
    @Transactional
    public void addReCommentAlarm(String commentWriter, String reCommentWriter, Long articleId, Long commentId) {

        ReCommentAlarmRequestDto reCommentAlarmRequestDto = ReCommentAlarmRequestDto.builder()
                .commentWriter(reCommentWriter)
                .reCommentWriter(reCommentWriter)
                .articleId(articleId)
                .commentId(commentId)
                .build();

        alarmMapper.insertReCommentAlarm(reCommentAlarmRequestDto);
    }

    //

}
