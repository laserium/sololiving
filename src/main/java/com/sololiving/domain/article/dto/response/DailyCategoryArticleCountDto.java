package com.sololiving.domain.article.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DailyCategoryArticleCountDto {
    private String date;
    private String categoryCode;
    private int dailyCategoryCount;
}
