package com.sololiving.domain.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sololiving.domain.vo.UserVo;

@Mapper
public interface UserMapper {
    // 회원가입
    void insertUser(UserVo userVo);

    // 회원탈퇴
    void deleteByUserId(@Param("userId") String userId);
}
