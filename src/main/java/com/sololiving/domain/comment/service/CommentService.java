package com.sololiving.domain.comment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sololiving.domain.article.exception.ArticleErrorCode;
import com.sololiving.domain.article.mapper.ArticleMapper;
import com.sololiving.domain.comment.dto.request.AddCommentRequestDto;
import com.sololiving.domain.comment.dto.request.AddReCommentRequestDto;
import com.sololiving.domain.comment.dto.request.UpdateCommentRequestDto;
import com.sololiving.domain.comment.exception.CommentErrorCode;
import com.sololiving.domain.comment.mapper.CommentMapper;
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
    private final ArticleMapper articleMapper;

    // 댓글 작성
    @Transactional
    public void addComment(AddCommentRequestDto requestDto, String writer) {
        if (!articleMapper.checkArticleExists(requestDto.getArticleId())) {
            throw new ErrorException(ArticleErrorCode.ARTICLE_NOT_FOUND);
        }
        Long articleId = requestDto.getArticleId();

        CommentVo commentVo = CommentVo.builder()
                .articleId(articleId)
                .parentCommentId(null)
                .writer(writer)
                .content(requestDto.getContent())
                .build();

        commentMapper.insertComment(commentVo);
        articleMapper.incrementCommentCount(articleId);
    }

    // 대댓글 작성
    @Transactional
    public void addReComment(AddReCommentRequestDto requestDto, String writer) {
        if (!articleMapper.checkArticleExists(requestDto.getArticleId())) {
            throw new ErrorException(ArticleErrorCode.ARTICLE_NOT_FOUND);
        }
        Long articleId = requestDto.getArticleId();
        CommentVo commentVo = CommentVo.builder()
                .articleId(articleId)
                .parentCommentId(requestDto.getParentCommentId())
                .writer(writer)
                .content(requestDto.getContent())
                .build();

        commentMapper.insertComment(commentVo);
        articleMapper.incrementCommentCount(articleId);
    }

    // 댓글 삭제
    public void removeComment(Long articleId, Long commentId, String writer) {
        validateRemoveComment(articleId, commentId, writer);
        deleteComment(articleId, commentId, writer);
    }

    private void validateRemoveComment(Long articleId, Long commentId, String writer) {
        if (!commentMapper.checkComment(commentId)) {
            throw new ErrorException(CommentErrorCode.NOT_FOUND_COMMENT);
        }
        if (!commentMapper.verifyCommentWriter(commentId, writer)) {
            throw new ErrorException(GlobalErrorCode.NO_PERMISSION);
        }
    }

    @Transactional
    private void deleteComment(Long articleId, Long commentId, String writer) {
        commentMapper.deleteComment(commentId);
        articleMapper.decrementCommentCount(articleId);
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

}