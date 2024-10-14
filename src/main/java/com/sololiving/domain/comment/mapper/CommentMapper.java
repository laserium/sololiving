package com.sololiving.domain.comment.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommentMapper {
    // 게시글 작성
    void insertComment(@Param("articleId") Long articleId, @Param("content") String content);
}
