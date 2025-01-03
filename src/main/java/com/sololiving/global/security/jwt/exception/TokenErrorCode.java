package com.sololiving.global.security.jwt.exception;

import com.sololiving.global.exception.error.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TokenErrorCode implements ErrorCode {

    NO_ACCESS_TOKEN("TOKEN_E001", "엑세스 토큰이 존재하지 않습니다."),
    CANNOT_FIND_RT("TOKEN_E002", "해당하는 유저의 리프레쉬 토큰을 찾을 수 없습니다."),
    CANNOT_DELETE_REFRESH_TOKEN("TOKEN_E003", "리프레쉬 토큰을 삭제할 수 없습니다."),
    CANNOT_FIND_AT("TOKEN_E004", "해당하는 유저의 엑세스토큰을 찾을 수 없습니다."),
    CANNOT_REFRESH_TOKEN("TOKEN_E005", "토큰 갱신 중 오류발생"),
    CANNT_EXTRACT_USER("TOKEN_E006", "엑세스 토큰에서 추출한 회원 데이터가 존재하지 않습니다."),
    INVALID_ACCESS_TOKEN("TOKEN_E007", "엑세스 토큰이 유효하지 않습니다."),
    INVALID_REFRESH_TOKEN("TOKEN_E008", "리프레쉬 토큰이 유효하지 않습니다.");

    private final String code;
    private final String message;

}
