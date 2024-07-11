package com.sololiving.domain.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sololiving.domain.vo.UserVo;

@Mapper
public interface UserMapper {
    UserVo findByUserId(@Param("userId") String userId);
}
