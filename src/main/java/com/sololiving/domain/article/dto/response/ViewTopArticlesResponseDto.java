package com.sololiving.domain.article.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ViewTopArticlesResponseDto {

    private int displayNumber;
    private Long articleId;
    private String thumbNailUrl;
    private String writer;
    private String title;
    private String content;
    private int likeCnt;
    private int viewCnt;
    private int commentCount;
    private int mediaType;
    private String timeAgo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }
}
