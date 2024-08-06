package com.sololiving.domain.user.dto.request;

import com.sololiving.domain.user.enums.Gender;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserGenderRequestDto {
    private Gender gender;
}
