package com.sololiving.domain.alarm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sololiving.domain.alarm.dto.request.CommentAlarmRequestDto;
import com.sololiving.domain.alarm.dto.request.ReCommentAlarmRequestDto;
import com.sololiving.domain.alarm.dto.response.ViewAlarmListResponseDto;

@Mapper
public interface AlarmMapper {

    // 게시글에 댓글 작성될 경우 댓글 알림 생성(CREATE)
    void insertCommentAlarm(CommentAlarmRequestDto commentAlarmRequestDto);

    // 댓글에 대댓글 작성될 경우 대댓글 알림 생성(CREATE)
    void insertReCommentAlarm(ReCommentAlarmRequestDto reCommentAlarmRequestDto);

    // 누군가 나를 팔로우 했을 경우 알림 생성(CREATE)
    void insertFollowAlarm(@Param("targetId") String targetId, @Param("userId") String userId);

    // 알림 리스트 조회(READ)
    List<ViewAlarmListResponseDto> selectAlarmListByUserId(@Param("userId") String userId);

    // 알림 읽기(UPDATE)
    int updateAlarmAsRead(@Param("alarmId") Long alarmId, @Param("userId") String userId);

    // 해당 알림이 특정 사용자의 것인지 확인
    boolean isAlarmOwnedByUser(@Param("alarmId") Long alarmId, @Param("userId") String userId);

    // 알림 삭제(DELETE)
    void deleteAlarmById(@Param("alarmId") Long alarmId);

    // 새로운 알림 개수 조회
    int countUnreadAlarms(@Param("userId") String userId);
}
