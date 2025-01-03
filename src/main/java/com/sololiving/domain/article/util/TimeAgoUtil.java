package com.sololiving.domain.article.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeAgoUtil {

    public static String getTimeAgo(LocalDateTime createdAt) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        ZonedDateTime createdAtZoned = createdAt.atZone(ZoneId.of("Asia/Seoul"));
        Duration duration = Duration.between(createdAtZoned, now);

        long seconds = duration.getSeconds();
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (seconds < 60) {
            return seconds + "초 전";
        } else if (minutes < 60) {
            return minutes + "분 전";
        } else if (hours < 24) {
            return "약 " + hours + "시간 전";
        } else if (days < 7) {
            return days + "일 전";
        } else {
            return createdAt.toLocalDate().toString(); // 일주일 이상이면 날짜만 표기
        }
    }
}
