package com.sololiving.domain.follow.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sololiving.domain.follow.dto.response.ViewFollowListResponseDto;
import com.sololiving.domain.follow.dto.response.ViewFollowerListResponseDto;

@Mapper
public interface FollowViewMapper {
    List<ViewFollowListResponseDto> selectFollowListByUserId(@Param("userId") String userId);

    List<ViewFollowerListResponseDto> selectFollowerListByUserId(@Param("userId") String userId);

}
