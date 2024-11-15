package com.sololiving.domain.log.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HourlyVisitDto {
    private int hour;
    private int visitCount;
}