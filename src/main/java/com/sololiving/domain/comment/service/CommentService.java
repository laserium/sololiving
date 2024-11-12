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
import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.domain.user.mapper.UserViewMapper;
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
    private final UserViewMapper userViewMapper;

    // 댓글 작성
    public void addComment(AddCommentRequestDto requestDto, String writer) {
        validateCommentRequest(requestDto.getArticleId());
        insertComment(requestDto.getArticleId(), null, writer, requestDto.getContent());
    }

    // 대댓글 작성
    public void addReComment(AddReCommentRequestDto requestDto, String writer) {
        validateCommentRequest(requestDto.getArticleId());
        // 탈퇴한 회원의 댓글인지 확인
        if (commentMapper.selectWriterStatusByCommentId(requestDto.getParentCommentId()).equals("WITHDRAW")) {
            throw new ErrorException(UserErrorCode.IS_DELETED_USER);
        }
        insertComment(requestDto.getArticleId(), requestDto.getParentCommentId(), writer, requestDto.getContent());
    }

    private void validateCommentRequest(Long articleId) {
        // 게시글 존재 여부 확인
        if (!articleMapper.checkArticleExists(articleId)) {
            throw new ErrorException(ArticleErrorCode.ARTICLE_NOT_FOUND);
        }
        // 탈퇴한 회원의 게시글인지 확인
        if (userViewMapper.isUserDeleted(articleMapper.selectWriterByArticleId(articleId))) {
            throw new ErrorException(UserErrorCode.IS_DELETED_USER);
        }

    }

    @Transactional
    private void insertComment(Long articleId, Long parentCommentId, String writer, String content) {
        CommentVo commentVo = CommentVo.builder()
                .articleId(articleId)
                .parentCommentId(parentCommentId)
                .writer(writer)
                .content(content)
                .build();

        commentMapper.insertComment(commentVo);
        articleMapper.incrementCommentCount(articleId);
    }

    // 댓글 삭제
    public void removeComment(Long commentId, String writer) {
        Long articleId = commentMapper.selectArticleIdByCommentId(commentId);
        if (articleId == null) {
            throw new ErrorException(ArticleErrorCode.ARTICLE_NOT_FOUND);
        }
        validateRemoveComment(commentId, writer);
        deleteComment(articleId, commentId, writer);
    }

    private void validateRemoveComment(Long commentId, String writer) {
        if (!commentMapper.checkComment(commentId)) {
            throw new ErrorException(CommentErrorCode.NOT_FOUND_COMMENT);
        }
        if (!commentMapper.verifyCommentWriter(commentId, writer)) {
            throw new ErrorException(GlobalErrorCode.NO_PERMISSION);
        }
        // 탈퇴한 회원의 게시글인지 확인
        if (commentMapper.selectWriterStatusByCommentId(commentId).equals("WITHDRAW")) {
            throw new ErrorException(UserErrorCode.IS_DELETED_USER);
        }
    }

    @Transactional
    private void deleteComment(Long articleId, Long commentId, String writer) {
        commentMapper.deleteComment(commentId);
        articleMapper.decrementCommentCount(articleId);
    }

    // 댓글 수정
    public void modifyComment(Long commentId, String writer, UpdateCommentRequestDto requestDto) {
        validateRemoveComment(commentId, writer);
        updateComment(commentId, writer, requestDto);
    }

    @Transactional
    public void updateComment(Long commentId, String writer, UpdateCommentRequestDto requestDto) {
        commentMapper.updateComment(commentId, requestDto.getContent());
    }

}