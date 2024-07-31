package com.sololiving.domain.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.sololiving.domain.user.dto.response.ViewUserListResponseDto;

@Mapper
public interface UserViewMapper {
    List<ViewUserListResponseDto> findUserList();
}
