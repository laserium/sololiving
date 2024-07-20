package com.sololiving.domain.user.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sololiving.domain.auth.dto.auth.request.SignUpRequestDto;
import com.sololiving.domain.vo.UserVo;

@Mapper
public interface UserMapper {
    
    void saveUser(SignUpRequestDto signUpRequestDto);

    Optional<UserVo> findByUserId(@Param("userId") String userId);
    
}