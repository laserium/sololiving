package com.sololiving.domain.auth.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sololiving.domain.vo.RefreshTokenVo;

@Mapper
public interface RefreshTokenMapper {
    Optional<RefreshTokenVo> findByUserId(@Param("userId") String userId);
    void save(@Param("refreshToken") RefreshTokenVo refreshToken);
}