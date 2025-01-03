package com.sololiving.domain.alarm.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentAlarmRequestDto {

    private String articleWriter;
    private String commentWriter;
    private Long articleId;
    private Long commentId;

}
