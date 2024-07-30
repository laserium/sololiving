package com.sololiving.domain.vo;

import com.sololiving.domain.user.enums.Gender;
import com.sololiving.domain.user.enums.Status;
import com.sololiving.domain.user.enums.UserType;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserVo {

    private String userId;
    private String userPwd;
    private String oauth2UserId;
    private String nickName;
    private String contact;
    private String email;
    private Gender gender;
    private String address;
    private LocalDate birth;
    private Status status;
    private String followersCnt;
    private String followingCnt;
    private UserType userType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastSignInAt;
    private LocalDateTime lastActivityAt;

}
