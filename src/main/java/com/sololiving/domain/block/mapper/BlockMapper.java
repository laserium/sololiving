package com.sololiving.domain.block.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BlockMapper {

    // 차단 하기
    void insertBlock(@Param("userId") String userId, @Param("targetId") String targetId);

    // 차단 유무 검증
    boolean existsBlock(@Param("userId") String userId, @Param("targetId") String targetId);

    // 차단 해제 하기
    void deleteBlock(@Param("userId") String userId, @Param("targetId") String targetId);
}
