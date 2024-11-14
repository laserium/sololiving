package com.sololiving.domain.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sololiving.domain.user.dto.response.ViewProfileHedaerResponseDto;
import com.sololiving.domain.user.dto.response.ViewUserProfileResponseDto;

@Mapper
public interface UserProfileViewMapper {

    // 유저 프로필 조회(마이페이지)
    ViewUserProfileResponseDto selectUserProfile(@Param("targetId") String targetId);

    // 유저 헤더 프로필 조회(헤더)
    ViewProfileHedaerResponseDto selectUserProfileHeader(@Param("userId") String userId);

}
