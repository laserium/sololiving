package com.sololiving.domain.comment.mapper;

import org.apache.ibatis.annotations.Param;

import com.sololiving.domain.comment.vo.CommentLikeVo;

public interface CommentLikeMapper {

    // 댓글 추천 추가
    void insertCommentLike(CommentLikeVo commentLikeVo);

    // 이미 추천한 댓글에 재추천 불가
    boolean hasUserLikedComment(@Param("commentId") Long commentId, @Param("userId") String userId);
}
