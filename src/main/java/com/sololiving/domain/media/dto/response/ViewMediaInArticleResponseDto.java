package com.sololiving.domain.media.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sololiving.domain.media.enums.MediaType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ViewMediaInArticleResponseDto {
    private Long articleId;
    private MediaType mediaType;
    private String mediaUrl;
    private String mediaName;
    private long fileSize;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
