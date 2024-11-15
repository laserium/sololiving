package com.sololiving.domain.log.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sololiving.domain.log.enums.AuthMethod;
import com.sololiving.domain.log.enums.BlockMethod;
import com.sololiving.domain.log.enums.BoardMethod;
import com.sololiving.domain.log.enums.FollowMethod;
import com.sololiving.domain.log.vo.UserActivityLogVo;

@Mapper
public interface UserActivityLogMapper {

        void insertActivityLog(UserActivityLogVo userActivityLogVo);

        void insertAuthLog(@Param("id") Long id, @Param("authMethod") AuthMethod authMethod);

        void insertArticleLog(
                        @Param("id") Long id,
                        @Param("articleId") Long articleId,
                        @Param("boardMethod") BoardMethod boardMethod);

        void insertCommentLog(
                        @Param("id") Long id,
                        @Param("commentId") Long commentId,
                        @Param("boardMethod") BoardMethod boardMethod);

        void insertFollowLog(
                        @Param("id") Long id,
                        @Param("targetId") String targetId,
                        @Param("boardMethod") FollowMethod followMethod);

        void insertBlockLog(
                        @Param("id") Long id,
                        @Param("targetId") String targetId,
                        @Param("boardMethod") BlockMethod blockMethod);

}
