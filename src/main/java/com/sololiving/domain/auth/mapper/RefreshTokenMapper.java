package com.sololiving.domain.auth.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sololiving.domain.vo.RefreshTokenVo;

@Mapper
public interface RefreshTokenMapper {
    Optional<RefreshTokenVo> findRefreshTokenByUserId(@Param("userId") String userId);
    
    void insert(RefreshTokenVo refreshToken);
    
    void update(RefreshTokenVo refreshToken);

    int deleteByRefreshToken(@Param("refreshToken") String refreshToken);

}
