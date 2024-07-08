package com.sololiving.domain.auth.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sololiving.domain.vo.UserVO;

@Mapper
public interface UserMapper {
    void save(@Param("user") UserVO userVO);
}
