package com.sololiving.domain.block.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sololiving.domain.block.dto.response.ViewBlockListResponseDto;

@Mapper
public interface BlockViewMapper {

    List<ViewBlockListResponseDto> selectBlockListByUserId(@Param("userId") String userId);

}
