package com.sololiving.domain.comment.dto.request;

import lombok.Getter;

@Getter
public class DeleteCommentRequestDto {

    private Long articleId;
    private Long commentId;

}
