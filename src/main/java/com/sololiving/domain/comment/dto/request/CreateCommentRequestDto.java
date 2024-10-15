package com.sololiving.domain.comment.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateCommentRequestDto {

    private Long articleId;
    private String content;

}
