package com.sololiving.domain.comment.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sololiving.domain.article.util.TimeAgoUtil;
import com.sololiving.domain.comment.dto.response.ViewCommentsResponseDto;
import com.sololiving.domain.comment.dto.response.ViewUsersCommentsResponseDto;
import com.sololiving.domain.comment.mapper.CommentViewMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentViewService {

    private final CommentViewMapper commentViewMapper;

    // 게시물의 댓글 조회
    public List<ViewCommentsResponseDto> viewComments(Long articleId, String userId) {
        List<ViewCommentsResponseDto> comments = commentViewMapper.selectAllComments(articleId, userId);
        comments.forEach(comment -> {
            String timeAgo = TimeAgoUtil.getTimeAgo(comment.getCreatedAt());
            comment.setTimeAgo(timeAgo);
        });
        return comments;
    }

    // 사용자의 댓글 조회
    public List<ViewUsersCommentsResponseDto> viewUserComments(String writer, String userId, String searchContent) {
        return commentViewMapper.selectUserComments(writer, userId,
                searchContent);
    }
}
