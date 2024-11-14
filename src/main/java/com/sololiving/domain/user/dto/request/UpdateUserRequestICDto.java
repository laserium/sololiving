package com.sololiving.domain.user.dto.request;

import java.time.LocalDate;

import com.sololiving.domain.user.enums.Gender;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class UpdateUserRequestICDto {

    @Getter
    @NoArgsConstructor
    public static class UpdateUserEmailRequestDto {
        private String email;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateUserContactRequestDto {
        private String contact;
        private String code;
    }

    @Getter
    @NoArgsConstructor
    public static class ValidateUpdateUserContactRequestDto {
        private String contact;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateUserAddressRequestDto {
        private String address;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateUserPasswordRequestDto {
        private String password;
        private String newPassword;
    }

    @Getter
    public static class UpdateUserNicknameRequestDto {
        private String nickname;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateUserGenderRequestDto {
        private Gender gender;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateUserBirthRequestDto {
        private LocalDate birth;
    }

}
