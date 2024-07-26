package com.sololiving.domain.user.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sololiving.domain.vo.UserVo;

@Mapper
public interface UserMapper {

    UserVo findByUserId(@Param("userId") String userId);
    
    UserVo findByOauth2UserId(@Param("oauth2UserId") String oauth2UserId);

    String findEmailByUserId(@Param("userId") String userId);

    String findPwdByIdAndEmail(@Param("userId") String userId, @Param("email") String email);

    UserVo findByEmail(@Param("email") String email);
}
