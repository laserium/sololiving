package com.sololiving.domain.auth.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sololiving.domain.vo.UserVo;

@Mapper
public interface RefreshTokenMapper {
    void updateRefreshToken(@Param("userId") String userId, @Param("refreshToken") String refreshToken);

    UserVo findByUserId(@Param("userId") String userId);
}
