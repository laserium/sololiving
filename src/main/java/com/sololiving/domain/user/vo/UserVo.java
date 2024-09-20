package com.sololiving.domain.user.vo;

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
    private String nickname;
    private String contact;
    private String email;
    private Gender gender;
    private String address;
    private LocalDate birth;
    private Status status;
    private int followersCnt;
    private int followingCnt;
    private String profileImage;
    private String profileBio;
    private int points;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserType userType;
    private LocalDateTime lastSignInAt;
    private LocalDateTime lastActivityAt;

}
