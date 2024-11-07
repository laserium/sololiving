package com.sololiving.domain.comment.vo;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentLikeVo {

    private Long commentId;
    private String userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
