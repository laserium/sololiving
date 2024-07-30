package com.sololiving.domain.user.dto.response;

import java.time.LocalDateTime;

import com.sololiving.domain.user.enums.Status;
import com.sololiving.domain.user.enums.UserType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ViewUserListResponseDto {

    private String userId;
    private String email;
    private String contact;
    private Status status;
    private UserType userType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastSignInAt;
    private LocalDateTime lastActivityAt;
    
}
