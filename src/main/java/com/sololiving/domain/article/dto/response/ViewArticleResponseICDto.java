package com.sololiving.domain.article.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sololiving.domain.article.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

public class ViewArticleResponseICDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ViewArticleDetailsResponseDto {

        private Long articleId;
        private String writer;
        private String title;
        private String content;
        private String categoryCode;
        private int likeCnt;
        private int viewCnt;
        private int score;
        private Status status;
        private String timeAgo;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdAt;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime updatedAt;

        public void setTimeAgo(String timeAgo) {
            this.timeAgo = timeAgo;
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ViewArticlesListResponseDto {
        private int displayNumber;
        private Long articleId;
        private String title;
        private String content;
        private int likeCnt;
        private int viewCnt;
        private String timeAgo;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdAt;

        public void setTimeAgo(String timeAgo) {
            this.timeAgo = timeAgo;
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ViewTopArticlesResponseDto {
        private Long articleId;
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
