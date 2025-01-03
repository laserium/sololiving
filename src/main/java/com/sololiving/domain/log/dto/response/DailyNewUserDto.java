package com.sololiving.domain.log.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DailyNewUserDto {
    private String signupDate;
    private int newUserCount;
}
