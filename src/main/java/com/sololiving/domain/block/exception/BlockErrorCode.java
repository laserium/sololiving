package com.sololiving.domain.block.exception;

import com.sololiving.global.exception.error.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BlockErrorCode implements ErrorCode {

    CANNOT_BLOCK_MYSELF("BLOCK_E001", "본인을 차단할 수 없습니다."),
    ALREADY_BLOCKED("BLOCK_E002", "이미 차단한 사용자입니다."),
    DID_NOT_BLOCKED("BLOCK_E003", "차단 상태가 아닌 사용자입니다."),
    CANNOT_UNBLOCK_MYSELF("BLOCK_E004", "본인을 차단 취소 할 수 없습니다.");

    private final String code;
    private final String message;
}
