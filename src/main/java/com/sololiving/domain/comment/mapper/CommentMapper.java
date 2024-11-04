package com.sololiving.domain.comment.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sololiving.domain.comment.vo.CommentVo;

@Mapper
public interface CommentMapper {
    // 댓글 작성
    void insertComment(CommentVo commentVo);

    // 댓글 삭제 - 작성자 검증
    boolean verifyCommentWriter(@Param("commentId") Long commentId, @Param("writer") String writer);

    // 댓글 존재 검증
    boolean checkComment(@Param("commentId") Long commentId);
}
