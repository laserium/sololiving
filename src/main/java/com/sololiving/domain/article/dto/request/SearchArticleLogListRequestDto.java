package com.sololiving.domain.article.dto.request;

import lombok.NoArgsConstructor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SearchArticleLogListRequestDto {

    private String status;
    private String categoryCode;
    private String searchTitle;
    private String searchWriter;
    private String sortBy;
    private String order = "asc"; // 기본 정렬 순서를 오름차순으로 설정
}
