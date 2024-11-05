package com.sololiving.domain.comment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sololiving.domain.article.exception.ArticleErrorCode;
import com.sololiving.domain.article.mapper.ArticleMapper;
import com.sololiving.domain.comment.dto.request.AddCommentRequestDto;
import com.sololiving.domain.comment.dto.request.AddReCommentRequestDto;
import com.sololiving.domain.comment.dto.request.UpdateCommentRequestDto;
import com.sololiving.domain.comment.exception.CommentErrorCode;
import com.sololiving.domain.comment.mapper.CommentLikeMapper;
import com.sololiving.domain.comment.mapper.CommentMapper;
import com.sololiving.domain.comment.vo.CommentLikeVo;
import com.sololiving.domain.comment.vo.CommentVo;
import com.sololiving.global.exception.GlobalErrorCode;
import com.sololiving.global.exception.error.ErrorException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {

    private final CommentMapper commentMapper;
    private final CommentLikeMapper commentLikeMapper;
    private final ArticleMapper articleMapper;

    // 댓글 작성
    @Transactional
    public void addComment(AddCommentRequestDto requestDto, String writer) {
        if (!articleMapper.checkArticleExists(requestDto.getArticleId())) {
            throw new ErrorException(ArticleErrorCode.ARTICLE_NOT_FOUND);
        }

        CommentVo commentVo = CommentVo.builder()
                .articleId(requestDto.getArticleId())
                .parentCommentId(null)
                .writer(writer)
                .content(requestDto.getContent())
                .build();

        commentMapper.insertComment(commentVo);
    }

    // 대댓글 작성
    @Transactional
    public void addReComment(AddReCommentRequestDto requestDto, String writer) {
        if (!articleMapper.checkArticleExists(requestDto.getArticleId())) {
            throw new ErrorException(ArticleErrorCode.ARTICLE_NOT_FOUND);
        }

        CommentVo commentVo = CommentVo.builder()
                .articleId(requestDto.getArticleId())
                .parentCommentId(requestDto.getParentCommentId())
                .writer(writer)
                .content(requestDto.getContent())
                .build();

        commentMapper.insertComment(commentVo);
    }

    // 댓글 삭제
    @Transactional
    public void removeComment(Long commentId, String writer) {
        if (!commentMapper.checkComment(commentId)) {
            throw new ErrorException(CommentErrorCode.NOT_FOUND_COMMENT);
        }
        if (!commentMapper.verifyCommentWriter(commentId, writer)) {
            throw new ErrorException(GlobalErrorCode.NO_PERMISSION);
        }
        commentMapper.deleteComment(commentId);
    }

    // 댓글 수정
    @Transactional
    public void updateComment(Long commentId, String writer, UpdateCommentRequestDto requestDto) {
        if (!commentMapper.checkComment(commentId)) {
            throw new ErrorException(CommentErrorCode.NOT_FOUND_COMMENT);
        }
        if (!commentMapper.verifyCommentWriter(commentId, writer)) {
            throw new ErrorException(GlobalErrorCode.NO_PERMISSION);
        }
        log.info(requestDto.getContent());
        commentMapper.updateComment(commentId, requestDto.getContent());
    }

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
        commentMapper.updateLikeCount(commentId);
    }
}