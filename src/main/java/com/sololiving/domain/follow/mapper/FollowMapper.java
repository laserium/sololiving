package com.sololiving.domain.follow.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FollowMapper {
    // 팔로우 상태 검증
    boolean existsFollowing(@Param("userId") String userId, @Param("followTargetId") String followTargetId);

    // 팔로우 추가
    void insertFollow(@Param("userId") String userId, @Param("followTargetId") String followTargetId);

    // 팔로우 끊기
    void deleteFollow(@Param("userId") String userId, @Param("unfollowTargetId") String unfollowTargetId);

}
