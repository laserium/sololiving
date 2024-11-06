package com.sololiving.domain.comment.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sololiving.domain.article.util.TimeAgoUtil;
import com.sololiving.domain.comment.dto.response.ViewCommentsResponseDto;
import com.sololiving.domain.comment.mapper.CommentViewMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentViewService {

    private final CommentViewMapper commentViewMapper;

    // 해당 게시물의 댓글 전부 조회
    public List<ViewCommentsResponseDto> viewComments(Long articleId) {
        List<ViewCommentsResponseDto> comments = commentViewMapper.selectAllComments(articleId);
        comments.forEach(comment -> {
            String timeAgo = TimeAgoUtil.getTimeAgo(comment.getCreatedAt());
            comment.setTimeAgo(timeAgo);
        });
        return comments;
    }

}
