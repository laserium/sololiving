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

    // 댓글 삭제
    void deleteComment(@Param("commentId") Long commentId);

    // 댓글 수정
    void updateComment(@Param("commentId") Long commentId, @Param("content") String content);

    // 댓글 추천 수 업데이트
    void updateLikeCount(@Param("commentId") Long commentId);

    // 댓글 작성자 조회
    String selectCommentWriter(@Param("commentId") Long commentId);
}
