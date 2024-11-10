package com.sololiving.domain.article.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sololiving.domain.article.enums.Status;
import com.sololiving.domain.media.dto.response.ViewMediaInArticleResponseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class ViewArticleResponseICDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ViewArticleDetailsResponseDto implements Serializable {

        private static final long serialVersionUID = 1000L;

        private Long articleId;
        private String writer;
        private String title;
        private String content;
        private String categoryCode;
        private int likeCnt;
        private int viewCnt;
        private int score;
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

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ViewTopArticlesResponseDto implements Serializable {

        private static final long serialVersionUID = 4000L;

        private Long articleId;
        private String writer;
        private String title;
        private int likeCnt;
        private int viewCnt;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdAt;

    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ViewCategoryArticlesResponseDto implements Serializable {

        private static final long serialVersionUID = 5000L;

        private Long articleId;
        private String writer;
        private String title;
        private int likeCnt;
        private int viewCnt;
        private String timeAgo;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdAt;

        public void setTimeAgo(String timeAgo) {
            this.timeAgo = timeAgo;
        }
    }
}
