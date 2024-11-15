package com.sololiving.domain.article.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DailyArticleCountDto {

    private String date;
    private int dailyCount;

}
