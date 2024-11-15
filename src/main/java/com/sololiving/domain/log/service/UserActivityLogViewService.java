package com.sololiving.domain.log.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sololiving.domain.log.dto.response.DailyNewUserDto;
import com.sololiving.domain.log.dto.response.DailyVisitDto;
import com.sololiving.domain.log.dto.response.HourlyVisitDto;
import com.sololiving.domain.log.dto.response.ViewUALogListResponseDto;
import com.sololiving.domain.log.enums.ActivityType;
import com.sololiving.domain.log.mapper.UserActivityLogViewMapper;
import com.sololiving.global.exception.GlobalErrorCode;
import com.sololiving.global.exception.error.ErrorException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserActivityLogViewService {

    private final UserActivityLogViewMapper userActivityLogViewMapper;

    public List<ViewUALogListResponseDto> getLogsByActivityType(ActivityType activityType, String searchUser) {
        switch (activityType) {
            case AUTH:
                return userActivityLogViewMapper.getAuthLogs(activityType, searchUser);
            case ARTICLE:
                return userActivityLogViewMapper.getArticleLogs(activityType, searchUser);
            case COMMENT:
                return userActivityLogViewMapper.getCommentLogs(activityType, searchUser);
            case FOLLOW:
                return userActivityLogViewMapper.getFollowLogs(activityType, searchUser);
            case BLOCK:
                return userActivityLogViewMapper.getBlockLogs(activityType, searchUser);
            default:
                throw new ErrorException(GlobalErrorCode.REQUEST_TYPE_IS_WRONG);
        }
    }

    public int getTodayActiveUsers() {
        return userActivityLogViewMapper.selectTodayActiveUsers();
    }

    public int getMonthlyNewUsers() {
        return userActivityLogViewMapper.selectMonthlyNewUsers();
    }

    public int getCurrentOnlineUsers() {
        return userActivityLogViewMapper.selectCurrentOnlineUsers();
    }

    public List<DailyVisitDto> getDailyVisitsLastMonth() {
        return userActivityLogViewMapper.selectDailyVisitsLastMonth();
    }

    public List<DailyNewUserDto> getDailyNewUsersLastMonth() {
        return userActivityLogViewMapper.selectDailyNewUsersLastMonth();
    }

    public List<HourlyVisitDto> getHourlyVisitsToday() {
        return userActivityLogViewMapper.selectHourlyVisitsToday();
    }
}
