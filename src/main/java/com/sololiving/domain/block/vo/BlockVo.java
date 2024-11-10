package com.sololiving.domain.block.vo;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class BlockVo {

    private Long id;
    private String blockerId;
    private String blockingId;
    private LocalDateTime createdAt;

}
