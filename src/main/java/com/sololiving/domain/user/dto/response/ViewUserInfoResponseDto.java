package com.sololiving.domain.user.dto.response;

import java.time.LocalDate;

import com.sololiving.domain.user.enums.Gender;

import lombok.Getter;

@Getter
public class ViewUserInfoResponseDto {

    private String userId;
    private String email;
    private String contact;
    private String nickname;
    private String address;
    private Gender gender;
    private LocalDate birth;

}
