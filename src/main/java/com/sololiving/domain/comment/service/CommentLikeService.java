package com.sololiving.domain.comment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sololiving.domain.block.exception.BlockErrorCode;
import com.sololiving.domain.block.mapper.BlockMapper;
import com.sololiving.domain.comment.exception.CommentErrorCode;
import com.sololiving.domain.comment.mapper.CommentLikeMapper;
import com.sololiving.domain.comment.mapper.CommentMapper;
import com.sololiving.domain.comment.vo.CommentLikeVo;
import com.sololiving.domain.log.enums.BoardMethod;
import com.sololiving.domain.log.service.UserActivityLogService;
import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.global.exception.GlobalErrorCode;
import com.sololiving.global.exception.error.ErrorException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeMapper commentLikeMapper;
    private final CommentMapper commentMapper;
    private final BlockMapper blockMapper;
    private final UserActivityLogService userActivityLogService;

    // 댓글 추천
    public void likeComment(Long commentId, String userId, String ipAddress) {
        validateLikeComment(commentId, userId);
        insertLikeComment(commentId, userId);

        // 사용자 행동 로그 처리
        userActivityLogService.insertCommentLog(userId, ipAddress, commentId, BoardMethod.LIKE);
    }

    private void validateLikeComment(Long commentId, String userId) {
        // 입력값 NULL 검증
        if (commentId == null) {
            throw new ErrorException(GlobalErrorCode.REQUEST_IS_NULL);
        }
        // 댓글 존재 유무 확인
        if (!commentMapper.checkComment(commentId)) {
            throw new ErrorException(CommentErrorCode.NOT_FOUND_COMMENT);
        }
        // 차단 유무 확인
        if (blockMapper.existsBlock(userId, commentMapper.selectCommentWriter(commentId))) {
            throw new ErrorException(BlockErrorCode.ALREADY_BLOCKED);
        }
        // 탈퇴한 회원의 댓글인지 확인
        if (commentMapper.selectWriterStatusByCommentId(commentId).equals("WITHDRAW")) {
            throw new ErrorException(UserErrorCode.IS_DELETED_USER);
        }
        // 본인이 작성한 댓글에 추천 불가
        if (commentMapper.selectCommentWriter(commentId).equals(userId)) {
            throw new ErrorException(CommentErrorCode.CANNOT_LIKE_MY_COMMENT);
        }
        // 이미 추천한 경우 다시 추천 불가
        if (commentLikeMapper.hasUserLikedComment(commentId, userId)) {
            throw new ErrorException(CommentErrorCode.CANNOT_LIKE_DUPLICATE);
        }
    }

    @Transactional
    private void insertLikeComment(Long commentId, String userId) {
        CommentLikeVo commentLike = CommentLikeVo.builder()
                .commentId(commentId)
                .userId(userId)
                .build();
        commentLikeMapper.insertCommentLike(commentLike);
        commentMapper.updateCommentLikeCount(commentId);
    }

    // 댓글 추천 취소
    public void unlike(Long commentId, String userId, String ipAddress) {
        validateUnLikeComment(commentId, userId);
        likeCommentCancle(commentId, userId);

        // 사용자 행동 로그 처리
        userActivityLogService.insertCommentLog(userId, ipAddress, commentId, BoardMethod.UNLIKE);
    }

    private void validateUnLikeComment(Long commentId, String userId) {
        // 입력값 NULL 검증
        if (!commentMapper.checkComment(commentId)) {
            throw new ErrorException(CommentErrorCode.NOT_FOUND_COMMENT);
        }
        // 탈퇴한 회원의 댓글인지 확인
        if (commentMapper.selectWriterStatusByCommentId(commentId).equals("WITHDRAW")) {
            throw new ErrorException(UserErrorCode.IS_DELETED_USER);
        }
        // 본인이 작성한 댓글에 추천 불가
        if (commentMapper.selectCommentWriter(commentId).equals(userId)) {
            throw new ErrorException(CommentErrorCode.CANNOT_DISLIKE_MY_COMMENT);
        }
        // 추천하지 않았는데 추천 취소할 경우
        if (!commentLikeMapper.hasUserLikedComment(commentId, userId)) {
            throw new ErrorException(CommentErrorCode.NOT_LIKED_COMMENT);
        }
    }

    @Transactional
    public void likeCommentCancle(Long commentId, String userId) {
        commentLikeMapper.deleteCommentLike(commentId, userId);
        commentMapper.updateCommentLikeCount(commentId);
    }
}
