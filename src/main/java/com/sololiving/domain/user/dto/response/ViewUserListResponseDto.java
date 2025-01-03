package com.sololiving.domain.user.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastSignInAt;

}
