package com.sololiving.domain.log.dto.response;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DailyNewUserDto {
    private Date visitDate;
    private int visitCount;
}
