package com.sololiving.domain.user.dto.request;

import java.time.LocalDate;

import com.sololiving.domain.user.enums.Gender;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UpdateUserRequestDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateUserEmailRequestDto {
        private String email;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateUserContactRequestDto {
        private String contact;
        private String code;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValidateUpdateUserContactRequestDto {
        private String contact;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateUserAddressRequestDto {
        private String address;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateUserPasswordRequestDto {
        private String password;
        private String newPassword;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateUserNicknameRequestDto {
        private String nickname;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateUserGenderRequestDto {
        private Gender gender;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateUserBirthRequestDto {
        private LocalDate birth;
    }

}
