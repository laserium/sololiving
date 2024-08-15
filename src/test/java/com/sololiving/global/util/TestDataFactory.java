package com.sololiving.global.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.sololiving.domain.user.enums.Gender;
import com.sololiving.domain.user.enums.Status;
import com.sololiving.domain.user.enums.UserType;
import com.sololiving.domain.user.vo.UserVo;

public class TestDataFactory {

    public static UserVo createTestUserVo(String oauth2UserId) {
        return UserVo.builder()
                .userId("testUser")
                .userPwd("1234")
                .oauth2UserId(oauth2UserId)
                .nickname("tester")
                .contact("01055554444")
                .email("testUser@naver.com")
                .gender(Gender.DEFAULT)
                .address("testersHome")
                .birth(LocalDate.now())
                .status(Status.ACTIVE)
                .followersCnt(0)
                .followingCnt(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .userType(UserType.GENERAL)
                .lastSignInAt(LocalDateTime.now())
                .lastActivityAt(LocalDateTime.now())
                .build();

    }

}
