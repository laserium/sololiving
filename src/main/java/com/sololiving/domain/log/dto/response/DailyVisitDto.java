package com.sololiving.domain.log.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DailyVisitDto {
    private String visitDate;
    private int visitCount;
}
