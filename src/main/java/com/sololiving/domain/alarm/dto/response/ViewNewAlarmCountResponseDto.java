package com.sololiving.domain.alarm.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ViewNewAlarmCountResponseDto {
    private int alarmCount;
}
