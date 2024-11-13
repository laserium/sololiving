package com.sololiving.domain.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sololiving.domain.user.dto.response.ViewUserProfileResponseDto;

@Mapper
public interface UserProfileViewMapper {

    ViewUserProfileResponseDto selectUserProfile(@Param("targetId") String targetId);

}
