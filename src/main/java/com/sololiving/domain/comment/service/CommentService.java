package com.sololiving.domain.comment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sololiving.domain.article.exception.ArticleErrorCode;
import com.sololiving.domain.article.mapper.ArticleMapper;
import com.sololiving.domain.comment.dto.request.CreateCommentRequestDto;
import com.sololiving.domain.comment.dto.request.CreateReCommentRequestDto;
import com.sololiving.domain.comment.mapper.CommentMapper;
import com.sololiving.domain.comment.vo.CommentVo;
import com.sololiving.global.exception.error.ErrorException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentMapper commentMapper;
    private final ArticleMapper articleMapper;

    // 댓글 작성
    @Transactional
    public void addComment(CreateCommentRequestDto requestDto, String writer) {
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
    public void addReComment(CreateReCommentRequestDto requestDto, String writer) {
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
}