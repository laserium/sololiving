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

    UserVo findByUserId(@Param("userId") String userId);

    UserVo findByOauth2UserId(@Param("oauth2UserId") String oauth2UserId);

    String findEmailByUserId(@Param("userId") String userId);

    String findUserIdByEmail(@Param("email") String email);

    String findPwdByIdAndEmail(@Param("userId") String userId, @Param("email") String email);

    UserVo findByEmail(@Param("email") String email);

    void updatePassword(@Param("userPwd") String userPwd, @Param("userId") String userId);

    UserType findUserTypeByUserId(@Param("userId") String userId);
}
