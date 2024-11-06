package com.sololiving.domain.comment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sololiving.domain.comment.exception.CommentErrorCode;
import com.sololiving.domain.comment.mapper.CommentLikeMapper;
import com.sololiving.domain.comment.mapper.CommentMapper;
import com.sololiving.domain.comment.vo.CommentLikeVo;
import com.sololiving.global.exception.error.ErrorException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeMapper commentLikeMapper;
    private final CommentMapper commentMapper;

    // 댓글 추천
    @Transactional
    public void likeComment(Long commentId, String userId) {
        if (!commentMapper.checkComment(commentId)) {
            throw new ErrorException(CommentErrorCode.NOT_FOUND_COMMENT);
        }
        // 본인이 작성한 댓글에 추천 불가
        if (commentMapper.selectCommentWriter(commentId).equals(userId)) {
            throw new ErrorException(CommentErrorCode.CANNOT_LIKE_MY_COMMENT);
        }
        // 이미 추천한 경우 다시 추천 불가
        if (commentLikeMapper.hasUserLikedComment(commentId, userId)) {
            throw new ErrorException(CommentErrorCode.CANNOT_LIKE_DUPLICATE);
        }
        CommentLikeVo commentLike = CommentLikeVo.builder()
                .commentId(commentId)
                .userId(userId)
                .build();

        commentLikeMapper.insertCommentLike(commentLike);
        commentMapper.updateCommentLikeCount(commentId);
    }

    // 댓글 추천 취소
    @Transactional
    public void likeCommentCancle(Long commentId, String userId) {
        if (!commentMapper.checkComment(commentId)) {
            throw new ErrorException(CommentErrorCode.NOT_FOUND_COMMENT);
        }
        // 추천하지 않았는데 추천 취소할 경우
        if (!commentLikeMapper.hasUserLikedComment(commentId, userId)) {
            throw new ErrorException(CommentErrorCode.NOT_LIKED_COMMENT);
        }
        commentLikeMapper.deleteCommentLike(commentId, userId);
        commentMapper.updateCommentLikeCount(commentId);
    }
}
