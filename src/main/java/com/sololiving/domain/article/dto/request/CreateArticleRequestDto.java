package com.sololiving.domain.article.dto.request;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateArticleRequestDto {

    private String title;
    private String content;
    private String categoryCode;
    private List<String> tempMediaUrls;

}
