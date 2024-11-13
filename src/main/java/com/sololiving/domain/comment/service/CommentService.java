package com.sololiving.domain.comment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sololiving.domain.alarm.service.AlarmService;
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

    private final AlarmService alarmService;
    private final CommentMapper commentMapper;
    private final ArticleMapper articleMapper;
    private final UserViewMapper userViewMapper;

    // 댓글 작성
    public void addComment(AddCommentRequestDto requestDto, String writer) {
        Long articleId = requestDto.getArticleId();
        // 검증
        String articleWriter = validateCommentRequest(articleId);

        // 댓글 작성
        Long commentId = insertComment(articleId, null, writer, requestDto.getContent());

        // 알람 생성
        alarmService.addCommentAlarm(articleWriter, writer, articleId, commentId);
    }

    private String validateCommentRequest(Long articleId) {
        // 게시글 존재 여부 확인
        if (!articleMapper.checkArticleExists(articleId)) {
            throw new ErrorException(ArticleErrorCode.ARTICLE_NOT_FOUND);
        }
        String articleWriter = articleMapper.selectWriterByArticleId(articleId);
        // 탈퇴한 회원의 게시글인지 확인
        if (userViewMapper.isUserDeleted(articleWriter)) {
            throw new ErrorException(UserErrorCode.IS_DELETED_USER);
        }
        return articleWriter;

    }

    @Transactional
    private Long insertComment(Long articleId, Long parentCommentId, String writer, String content) {
        CommentVo commentVo = CommentVo.builder()
                .articleId(articleId)
                .parentCommentId(parentCommentId)
                .writer(writer)
                .content(content)
                .build();

        commentMapper.insertComment(commentVo);
        articleMapper.incrementCommentCount(articleId);

        return commentVo.getCommentId();
    }

    // 대댓글 작성
    public void addReComment(AddReCommentRequestDto requestDto, String writer) {
        // 검증
        validateReCommentRequest(requestDto.getArticleId());

        Long parentCommentId = requestDto.getParentCommentId();
        Long articleId = requestDto.getArticleId();

        // 탈퇴한 회원의 댓글인지 확인
        if (commentMapper.selectWriterStatusByCommentId(parentCommentId).equals("WITHDRAW")) {
            throw new ErrorException(UserErrorCode.IS_DELETED_USER);
        }

        // 대댓글 저장
        Long commentId = insertComment(articleId, parentCommentId, writer, requestDto.getContent());

        alarmService.addReCommentAlarm(writer, writer, articleId, commentId);
    }

    private void validateReCommentRequest(Long articleId) {
        // 게시글 존재 여부 확인
        if (!articleMapper.checkArticleExists(articleId)) {
            throw new ErrorException(ArticleErrorCode.ARTICLE_NOT_FOUND);
        }
        // 탈퇴한 회원의 게시글인지 확인
        if (userViewMapper.isUserDeleted(articleMapper.selectWriterByArticleId(articleId))) {
            throw new ErrorException(UserErrorCode.IS_DELETED_USER);
        }
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