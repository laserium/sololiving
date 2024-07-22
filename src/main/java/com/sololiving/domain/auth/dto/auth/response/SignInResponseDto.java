package com.sololiving.domain.auth.dto.auth.response;

import java.time.Duration;

import com.sololiving.domain.auth.enums.ClientId;
import com.sololiving.global.common.enums.UserType;

import lombok.Builder;
import lombok.Getter;

// 로그인 ResponseDto
@Getter
@Builder
public class SignInResponseDto {
    private Duration expiresIn;
    private UserType userType;
    private ClientId clientId;
}