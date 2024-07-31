package com.sololiving.domain.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sololiving.domain.user.enums.Status;
import com.sololiving.domain.vo.UserVo;

@Mapper
public interface UserMapper {
    // 회원가입
    void insertUser(UserVo userVo);

    // 회원탈퇴
    void deleteByUserId(@Param("userId") String userId);

    // 회원 상태 변경
    void updateUserStatus(@Param("userId") String userId, @Param("status") Status status);
}
