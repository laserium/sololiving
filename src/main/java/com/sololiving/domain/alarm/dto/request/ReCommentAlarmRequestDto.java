package com.sololiving.domain.alarm.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReCommentAlarmRequestDto {

    private String commentWriter;
    private String reCommentWriter;
    private Long articleId;
    private Long commentId;

}
