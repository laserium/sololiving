package com.sololiving.domain.article.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sololiving.domain.article.enums.Status;
import com.sololiving.domain.media.dto.response.ViewMediaInArticleResponseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ViewArticleDetailsResponseDto {

    private Long articleId;
    private String writer;
    private String title;
    private String content;
    private String categoryCode;
    private int likeCnt;
    private int viewCnt;
    private int score;
    private int commentCount;
    private int mediaType;
    private List<ViewMediaInArticleResponseDto> mediaList;
    private Status status;
    private String timeAgo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }

    public void setMediaList(List<ViewMediaInArticleResponseDto> mediaList) {
        this.mediaList = mediaList;
    }
}
