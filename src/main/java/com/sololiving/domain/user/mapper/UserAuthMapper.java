package com.sololiving.domain.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sololiving.domain.user.enums.UserType;
import com.sololiving.domain.user.vo.UserVo;

@Mapper
public interface UserAuthMapper {

    // 유효성검사(중복확인) - 아이디
    boolean existsByUserId(String userId);

    // 유효성검사(중복확인) - 이메일
    boolean existsByEmail(String email);

    // 유효성검사(중복확인) - 연락처
    boolean existsByContact(String contact);

    UserVo selectByUserId(@Param("userId") String userId);

    UserVo selectByOauth2UserId(@Param("oauth2UserId") String oauth2UserId);

    String selectPasswordByUserId(@Param("userId") String userId);

    String selectEmailByUserId(@Param("userId") String userId);

    String selectUserIdByEmail(@Param("email") String email);

    String selectPwdByIdAndEmail(@Param("userId") String userId, @Param("email") String email);

    UserVo selectByEmail(@Param("email") String email);

    void updatePassword(@Param("userPwd") String userPwd, @Param("userId") String userId);

    UserType selectUserTypeByUserId(@Param("userId") String userId);
}
