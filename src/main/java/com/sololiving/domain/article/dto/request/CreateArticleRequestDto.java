package com.sololiving.domain.article.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder    
public class CreateArticleRequestDto {
    private String title;
    private String contenet;
    
}
