package com.sololiving.domain.comment.service;

import org.springframework.stereotype.Service;

import com.sololiving.domain.comment.dto.request.CreateCommentRequestDto;
import com.sololiving.domain.comment.mapper.CommentMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentMapper commentMapper;

    public void addComment(CreateCommentRequestDto requestDto, String writer) {
        commentMapper.insertComment();
    }
}