package com.sololiving.domain.log.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sololiving.domain.log.dto.response.DailyNewUserDto;
import com.sololiving.domain.log.dto.response.DailyVisitDto;
import com.sololiving.domain.log.dto.response.HourlyVisitDto;
import com.sololiving.domain.log.dto.response.ViewUALogListResponseDto;
import com.sololiving.domain.log.enums.ActivityType;

@Mapper
public interface UserActivityLogViewMapper {

        List<ViewUALogListResponseDto> getAuthLogs(
                        @Param("activityType") ActivityType activityType,
                        @Param("searchUser") String searchUser);

        List<ViewUALogListResponseDto> getArticleLogs(
                        @Param("activityType") ActivityType activityType,
                        @Param("searchUser") String searchUser);

        List<ViewUALogListResponseDto> getCommentLogs(
                        @Param("activityType") ActivityType activityType,
                        @Param("searchUser") String searchUser);

        List<ViewUALogListResponseDto> getFollowLogs(
                        @Param("activityType") ActivityType activityType,
                        @Param("searchUser") String searchUser);

        List<ViewUALogListResponseDto> getBlockLogs(
                        @Param("activityType") ActivityType activityType,
                        @Param("searchUser") String searchUser);

        int selectTodayActiveUsers();

        int selectMonthlyNewUsers();

        int selectCurrentOnlineUsers();

        List<DailyVisitDto> selectDailyVisitsLastMonth();

        List<DailyNewUserDto> selectDailyNewUsersLastMonth();

        List<HourlyVisitDto> selectHourlyVisitsToday();

}
