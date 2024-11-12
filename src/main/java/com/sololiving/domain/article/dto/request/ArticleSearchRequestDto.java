package com.sololiving.domain.article.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ArticleSearchRequestDto {
    private String categoryCode;
    private int page = 0;
    private String userId; // 조회하는 주체
    private String writer; // 조회되는 작성자
    private String sort = "recent";
    private String searchTitle;
    private String searchContents;
    private String searchWriter;
    private int limit = 20;

}
