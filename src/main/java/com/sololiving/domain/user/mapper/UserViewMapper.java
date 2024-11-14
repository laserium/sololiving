package com.sololiving.domain.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sololiving.domain.user.dto.response.ViewUserInfoResponseDto;

@Mapper
public interface UserViewMapper {

    ViewUserInfoResponseDto viewUserInformation(@Param("userId") String userId);

    // 탈퇴한 회원인지 확인하기 - 탈퇴한 회원 : true 리턴
    boolean isUserDeleted(String userId);

}
