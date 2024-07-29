package com.sololiving.domain.auth.dto.auth.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PasswordResetRequestDto {
    
    private String userId;
    private String email;

}
