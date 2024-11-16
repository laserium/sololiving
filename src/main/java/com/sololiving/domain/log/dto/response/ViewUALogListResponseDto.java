package com.sololiving.domain.log.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sololiving.domain.log.enums.ActivityType;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ViewUALogListResponseDto {

    private Long id;
    private String userId;
    private String ipAddress;
    private ActivityType activityType;
    private Long boardId;
    private String targetId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
