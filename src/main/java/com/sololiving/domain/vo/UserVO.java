package com.sololiving.domain.vo;

import com.sololiving.domain.user.enums.Gender;
import com.sololiving.global.common.enums.UserType;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserVo {

    private String userId;
    private String userPwd;
    private String nickName;
    private String contact;
    private String email;
    private Gender gender;
    private String address;
    private LocalDate birth;
    private boolean is_active;
    private String followersCnt;
    private String followingCnt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserType userType;
    public UserVo orElseThrow(Object object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'orElseThrow'");
    }

}
