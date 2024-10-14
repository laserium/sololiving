package com.sololiving.domain.comment.vo;

import java.time.LocalDateTime;

import com.sololiving.domain.comment.enums.Status;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentVo {

    private Long commentId;
    private Long articleId;
    private String writer;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int likeCnt;
    private Status status;

}
