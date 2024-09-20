package com.sololiving.domain.media.vo;

import java.time.LocalDateTime;

import com.sololiving.domain.media.enums.MediaType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MediaVo {
    private Long mediaId;
    private Long articleId;
    private MediaType mediaType;
    private String mediaUrl;
    private String mediaName;
    private long fileSize;
    private LocalDateTime createdAt;
}
