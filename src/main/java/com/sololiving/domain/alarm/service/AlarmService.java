package com.sololiving.domain.alarm.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sololiving.domain.alarm.dto.request.CommentAlarmRequestDto;
import com.sololiving.domain.alarm.dto.request.ReCommentAlarmRequestDto;
import com.sololiving.domain.alarm.dto.response.ViewAlarmListResponseDto;
import com.sololiving.domain.alarm.exception.AlarmErrorCode;
import com.sololiving.domain.alarm.mapper.AlarmMapper;
import com.sololiving.global.exception.error.ErrorException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmMapper alarmMapper;

    // 게시글에 댓글 작성될 경우 댓글 알림 생성
    @Transactional
    public void addCommentAlarm(String articleWriter, String commentWriter, Long articleId, Long commentId) {
        // 내 글에 내가 댓글 작성했을 경우 알림 생성 안 함
        if (articleWriter.equals(commentWriter)) {
            return;
        }
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
        // 내 댓글에 내가 대댓글 작성했을 경우 알림 생성 안 함
        if (commentWriter.equals(reCommentWriter)) {
            return;
        }
        ReCommentAlarmRequestDto reCommentAlarmRequestDto = ReCommentAlarmRequestDto.builder()
                .commentWriter(commentWriter)
                .reCommentWriter(reCommentWriter)
                .articleId(articleId)
                .commentId(commentId)
                .build();

        alarmMapper.insertReCommentAlarm(reCommentAlarmRequestDto);
    }

    // 누군가 나를 팔로우 했을 경우 알림 생성
    @Transactional
    public void addFollowAlarm(String targetId, String userId) {
        alarmMapper.insertFollowAlarm(targetId, userId);
    }

    // 알림 리스트 조회
    public List<ViewAlarmListResponseDto> getAlarmList(String userId) {
        return alarmMapper.selectAlarmListByUserId(userId);
    }

    // 알림 읽기
    @Transactional
    public void readAlarm(Long alarmId, String userId) {
        if (!alarmMapper.isAlarmOwnedByUser(alarmId, userId)) {
            throw new ErrorException(AlarmErrorCode.NO_PERMISSION_TO_VIEW_ALARM);
        }
        alarmMapper.updateAlarmAsRead(alarmId, userId);
    }

    @Transactional
    public void removeAlarm(Long alarmId, String userId) {
        // 알림이 해당 사용자에게 속해 있는지 확인
        if (!alarmMapper.isAlarmOwnedByUser(alarmId, userId)) {
            throw new ErrorException(AlarmErrorCode.NO_PERMISSION_TO_VIEW_ALARM);
        }
        alarmMapper.deleteAlarmById(alarmId);
    }

}
