package com.sololiving.domain.follow.mapper;

import com.sololiving.domain.follow.vo.FollowVo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FollowMapper {
    void insertFollow(FollowVo followVo);

    void deleteFollow(FollowVo followVo);

    String isAlreadyFollowing(String follower, String following);

    List<String> getFollowingList(String userId);

    List<String> getFollowerList(String userId);
}
