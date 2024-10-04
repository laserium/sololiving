package com.sololiving.global.security.jwt.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sololiving.global.security.jwt.vo.RefreshTokenVo;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface RefreshTokenMapper {
    RefreshTokenVo selectRefreshTokenByUserId(@Param("userId") String userId);

    void insert(RefreshTokenVo refreshToken);

    void update(RefreshTokenVo refreshToken);

    int deleteByRefreshToken(@Param("refreshToken") String refreshToken);

    boolean existsByUserId(@Param("userId") String userId);

    List<RefreshTokenVo> selectExpiredTokens(@Param("now") LocalDateTime now);

    void updateExpiredTokens(@Param("ids") List<Long> ids);

}
