package com.sololiving.domain.comment.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddCommentRequestDto {

    private Long articleId;
    private String content;

}
