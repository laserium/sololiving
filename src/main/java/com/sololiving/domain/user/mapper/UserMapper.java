package com.sololiving.domain.user.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sololiving.domain.vo.UserVo;

@Mapper
public interface UserMapper {

    Optional<UserVo> findByUserId(@Param("userId") String userId);
    
    Optional<UserVo> findByOauth2UserId(@Param("oauth2UserId") String oauth2UserId);
}
