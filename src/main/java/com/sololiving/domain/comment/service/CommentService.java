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
import com.sololiving.domain.log.enums.BoardMethod;
import com.sololiving.domain.log.service.UserActivityLogService;
import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.domain.user.mapper.UserSettingMapper;
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
    private final UserSettingMapper userSettingMapper;
    private final UserActivityLogService userActivityLogService;

    // 댓글 작성
    public void addComment(AddCommentRequestDto requestDto, String writer, String ipAddress) {
        Long articleId = requestDto.getArticleId();
        // 검증
        String articleWriter = validateCommentRequest(articleId);

        // 댓글 작성
        Long commentId = insertComment(articleId, null, writer, requestDto.getContent());

        // commentId가 null인지 확인
        if (commentId == null) {
            throw new ErrorException(GlobalErrorCode.REQUEST_IS_NULL);
        }
        // 알람 생성
        if (userSettingMapper.isPushNotificationSharingEnabled(articleWriter)) {
            alarmService.addCommentAlarm(articleWriter, writer, articleId, commentId);
        }

        // 사용자 행동 로그 처리
        userActivityLogService.insertCommentLog(writer, ipAddress, commentId, BoardMethod.CREATE);
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
        System.out.println(commentVo.getCommentId());
        return commentVo.getCommentId();
    }

    // 대댓글 작성
    public void addReComment(AddReCommentRequestDto requestDto, String reCommentWriter, String ipAddress) {
        // 검증
        validateReCommentRequest(requestDto.getParentCommentId(), requestDto.getArticleId(), reCommentWriter);

        Long parentCommentId = requestDto.getParentCommentId();
        Long articleId = requestDto.getArticleId();
        String commentWriter = commentMapper.selectCommentWriter(parentCommentId);

        // 탈퇴한 회원의 댓글인지 확인
        if (commentMapper.selectWriterStatusByCommentId(parentCommentId).equals("WITHDRAW")) {
            throw new ErrorException(UserErrorCode.IS_DELETED_USER);
        }

        // 대댓글 저장
        Long commentId = insertComment(articleId, parentCommentId, reCommentWriter, requestDto.getContent());
        if (userSettingMapper.isPushNotificationSharingEnabled(commentWriter)) {
            alarmService.addReCommentAlarm(commentWriter, reCommentWriter, articleId, commentId);
        }

        // 사용자 행동 로그 처리
        userActivityLogService.insertCommentLog(reCommentWriter, ipAddress, commentId, BoardMethod.CREATE);

    }

    private void validateReCommentRequest(Long parentCommentId, Long articleId, String reCommentWriter) {
        if (reCommentWriter == null) {
            throw new ErrorException(GlobalErrorCode.REQUEST_IS_NULL);
        }
        // 게시글 존재 여부 확인
        if (!articleMapper.checkArticleExists(articleId)) {
            throw new ErrorException(ArticleErrorCode.ARTICLE_NOT_FOUND);
        }
        // 탈퇴한 회원의 게시글인지 확인
        if (userViewMapper.isUserDeleted(articleMapper.selectWriterByArticleId(articleId))) {
            throw new ErrorException(UserErrorCode.IS_DELETED_USER);
        }
        if (parentCommentId != null) {
            Long parentCommentArticleId = commentMapper.selectArticleIdByCommentId(parentCommentId);
            if (!articleId.equals(parentCommentArticleId)) {
                throw new ErrorException(ArticleErrorCode.INVALID_PARENT_COMMENT);
            }
        }

    }

    // 댓글 삭제
    public void removeComment(Long commentId, String writer, String ipAddress) {
        Long articleId = commentMapper.selectArticleIdByCommentId(commentId);
        if (articleId == null) {
            throw new ErrorException(ArticleErrorCode.ARTICLE_NOT_FOUND);
        }
        validateRemoveComment(commentId, writer);
        deleteComment(articleId, commentId, writer);
        // 사용자 행동 로그 처리
        userActivityLogService.insertCommentLog(writer, ipAddress, commentId, BoardMethod.DELETE);

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
    public void modifyComment(Long commentId, String writer, UpdateCommentRequestDto requestDto, String ipAddress) {
        validateRemoveComment(commentId, writer);
        updateComment(commentId, writer, requestDto);
        // 사용자 행동 로그 처리
        userActivityLogService.insertCommentLog(writer, ipAddress, commentId, BoardMethod.UPDATE);

    }

    @Transactional
    public void updateComment(Long commentId, String writer, UpdateCommentRequestDto requestDto) {
        commentMapper.updateComment(commentId, requestDto.getContent());
    }

}