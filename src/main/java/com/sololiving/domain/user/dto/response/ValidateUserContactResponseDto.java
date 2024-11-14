package com.sololiving.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

// 회원 연락처 변경 - 인증 번호 검증 responseDto
@Getter
@Builder
public class ValidateUserContactResponseDto {
    private String code;
}
