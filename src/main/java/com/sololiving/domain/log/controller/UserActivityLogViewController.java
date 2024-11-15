package com.sololiving.domain.log.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.log.dto.response.DailyNewUserDto;
import com.sololiving.domain.log.dto.response.DailyVisitDto;
import com.sololiving.domain.log.dto.response.HourlyVisitDto;
import com.sololiving.domain.log.dto.response.ViewUALogListResponseDto;
import com.sololiving.domain.log.enums.ActivityType;
import com.sololiving.domain.log.service.UserActivityLogViewService;
import com.sololiving.global.aop.admin.AdminOnly;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-activity-log")
public class UserActivityLogViewController {

    private final UserActivityLogViewService userActivityLogViewService;

    // 사용자 행동 로그 목록 조회
    @AdminOnly
    @GetMapping("/{activityType}")
    public ResponseEntity<List<ViewUALogListResponseDto>> getLogsByActivityType(
            @PathVariable ActivityType activityType,
            @RequestParam(required = false) String searchUser, HttpServletRequest httpServletRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userActivityLogViewService.getLogsByActivityType(activityType, searchUser));
    }

    // 오늘 기준 오늘 사용했던 사람들 수 조회
    @AdminOnly
    @GetMapping("/today-active-users")
    public ResponseEntity<Integer> getTodayActiveUsers(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(userActivityLogViewService.getTodayActiveUsers());
    }

    // 현재 기준 달에 회원가입한 사람들 수
    @AdminOnly
    @GetMapping("/monthly-new-users")
    public ResponseEntity<Integer> getMonthlyNewUsers(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(userActivityLogViewService.getMonthlyNewUsers());
    }

    // 현재 접속자 수 (최근 5분 내에 활동한 사용자)
    @AdminOnly
    @GetMapping("/current-online-users")
    public ResponseEntity<Integer> getCurrentOnlineUsers(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(userActivityLogViewService.getCurrentOnlineUsers());
    }

    // 최근 한달간 일일 방문자 수
    @AdminOnly
    @GetMapping("/daily-visits-last-month")
    public ResponseEntity<List<DailyVisitDto>> getDailyVisitsLastMonth(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(userActivityLogViewService.getDailyVisitsLastMonth());
    }

    // 최근 한달간 일별 신규 가입자 수
    @AdminOnly
    @GetMapping("/daily-new-users-last-month")
    public ResponseEntity<List<DailyNewUserDto>> getDailyNewUsersLastMonth(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(userActivityLogViewService.getDailyNewUsersLastMonth());
    }

    // 오늘 하루간 시간대별 접속자 수
    @AdminOnly
    @GetMapping("/hourly-visits-today")
    public ResponseEntity<List<HourlyVisitDto>> getHourlyVisitsToday(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(userActivityLogViewService.getHourlyVisitsToday());
    }

}
