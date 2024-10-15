package com.sololiving.domain.comment.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.sololiving.domain.comment.vo.CommentVo;

@Mapper
public interface CommentMapper {
    // 게시글 작성
    void insertComment(CommentVo commentVo);
}
