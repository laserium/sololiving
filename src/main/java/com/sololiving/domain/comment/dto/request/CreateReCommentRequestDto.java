package com.sololiving.domain.comment.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateReCommentRequestDto {

    private Long articleId;
    private Long parentCommentId;
    private String content;

}
