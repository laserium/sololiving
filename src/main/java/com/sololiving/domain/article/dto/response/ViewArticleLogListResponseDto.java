package com.sololiving.domain.article.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ViewArticleLogListResponseDto {

    private Long articleId;
    private String title;
    private String categoryCode;
    private String writer;
    private int viewCnt;
    private int likeCnt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private String status;

}
