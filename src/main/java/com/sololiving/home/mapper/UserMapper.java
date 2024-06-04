package com.sololiving.home.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sololiving.home.vo.UserVO;

@Mapper
public interface UserMapper {
    void save(@Param("user") UserVO userVO);
}
