package com.sololiving.domain.block.exception;

import com.sololiving.global.exception.success.SuccessCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BlockSuccessCode implements SuccessCode {

    BLOCK_SUCCESS("BLOCK_S001", "차단 성공"),
    UNBLOCK_SUCCESS("BLOCK_S002", "차단 해제 완료");

    private final String code;
    private final String message;

}