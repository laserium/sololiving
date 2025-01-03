package com.sololiving.domain.comment.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ViewCommentsResponseDto {

    private Long commentId;
    private Long parentCommentId;
    private Long articleId;
    private String writer;
    private String content;
    private int likeCnt;
    private boolean isLiked;
    private String timeAgo;
    private String parentWriter;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }
}
