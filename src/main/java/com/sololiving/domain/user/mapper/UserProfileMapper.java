package com.sololiving.domain.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sololiving.domain.user.dto.response.UserProfileImageResponseDto;

@Mapper
public interface UserProfileMapper {
    // 프로필 이미지 생성
    void insertUserProfileImage(UserProfileImageResponseDto profileImage);

    // 프로필 이미지 수정
    void updateUserProfileImage(UserProfileImageResponseDto profileImage);

    // 프로필 설명 수정
    void updateProfileBio(@Param("userId") String userId, @Param("bio") String bio);

}
