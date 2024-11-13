package com.sololiving.domain.alarm.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.alarm.dto.response.ViewAlarmListResponseDto;
import com.sololiving.domain.alarm.dto.response.ViewNewAlarmCountResponseDto;
import com.sololiving.domain.alarm.exception.AlarmSuccessCode;
import com.sololiving.domain.alarm.service.AlarmService;
import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.domain.user.service.UserAuthService;
import com.sololiving.global.exception.ResponseMessage;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.util.SecurityUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequiredArgsConstructor
@RequestMapping("/alarm")
public class AlarmController {

    private final AlarmService alarmService;
    private final UserAuthService userAuthService;

    // 알림 리스트 조회
    @GetMapping("/list")
    public ResponseEntity<List<ViewAlarmListResponseDto>> viewAlarmList(HttpServletRequest httpServletRequest) {
        String userId = SecurityUtil.getCurrentUserId();
        if (userAuthService.isUserIdAvailable(userId)) {
            throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
        }
        List<ViewAlarmListResponseDto> alarmList = alarmService.getAlarmList(userId);
        return ResponseEntity.status(HttpStatus.OK).body(alarmList);
    }

    // 알림 읽기
    @PatchMapping("/{alarmId}")
    public ResponseEntity<?> readAlarm(@PathVariable Long alarmId, HttpServletRequest httpServletRequest) {
        String userId = SecurityUtil.getCurrentUserId();
        if (userAuthService.isUserIdAvailable(userId)) {
            throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
        }
        alarmService.readAlarm(alarmId, userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage.createSuccessResponse(AlarmSuccessCode.SUCCESS_TO_READ_ALARM));
    }

    // 알림 삭제
    @DeleteMapping("/{alarmId}")
    public ResponseEntity<?> removeAlarm(@PathVariable Long alarmId, HttpServletRequest httpServletRequest) {
        String userId = SecurityUtil.getCurrentUserId();
        if (userAuthService.isUserIdAvailable(userId)) {
            throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
        }
        alarmService.removeAlarm(alarmId, userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage.createSuccessResponse(AlarmSuccessCode.SUCCESS_TO_DELETE_ALARM));
    }

    // 새로운 알림 개수 조회
    @GetMapping("/count")
    public ResponseEntity<ViewNewAlarmCountResponseDto> getAlarmCount(HttpServletRequest httpServletRequest) {
        String userId = SecurityUtil.getCurrentUserId();
        if (userAuthService.isUserIdAvailable(userId)) {
            throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(alarmService.getAlarmCount(userId));
    }

}
