package com.sololiving.domain.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.sololiving.domain.user.dto.response.ViewUserListResponseDto;

@Mapper
public interface UserViewMapper {
    List<ViewUserListResponseDto> selectUserList();

    // 탈퇴한 회원인지 확인하기 - 탈퇴한 회원 : true 리턴
    boolean isUserDeleted(String userId);
}
