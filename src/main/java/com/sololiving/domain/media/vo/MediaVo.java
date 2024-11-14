package com.sololiving.domain.media.vo;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MediaVo {
    private Long mediaId;
    private Long articleId;
    private int mediaType;
    private String mediaUrl;
    private String mediaName;
    private long fileSize;
    private LocalDateTime createdAt;
}
