package com.sololiving.domain.auth.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.sololiving.domain.vo.UserVo;

@Mapper
public interface AuthMapper {
    // 회원가입
    void insertUser(UserVo userVo);

    // 유효성검사(중복확인) - 아이디
    boolean existsByUserId(String userId);

    // 유효성검사(중복확인) - 이메일
    boolean existsByEmail(String email);
    
    // 유효성검사(중복확인) - 연락처
    boolean existsByContact(String contact);
}
