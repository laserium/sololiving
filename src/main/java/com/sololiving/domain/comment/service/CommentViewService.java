package com.sololiving.domain.comment.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sololiving.domain.article.util.TimeAgoUtil;
import com.sololiving.domain.comment.dto.response.ViewCommentsResponseDto;
import com.sololiving.domain.comment.dto.response.ViewUsersCommentsResponseDto;
import com.sololiving.domain.comment.mapper.CommentViewMapper;
import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.domain.user.exception.user_setting.UserSettingErrorCode;
import com.sololiving.domain.user.mapper.UserSettingMapper;
import com.sololiving.domain.user.service.UserAuthService;
import com.sololiving.global.exception.error.ErrorException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentViewService {

    private final CommentViewMapper commentViewMapper;
    private final UserSettingMapper userSettingMapper;
    private final UserAuthService userAuthService;

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

        // 회원 유무 검증
        if (userAuthService.isUserIdAvailable(writer)) {
            throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
        }

        // 사용자 설정 필터링
        if (!userSettingMapper.isCommentSharingEnabled(writer)) {
            throw new ErrorException(UserSettingErrorCode.COMMENT_SHARING_DISABLED);
        }

        return commentViewMapper.selectUserComments(writer, userId,
                searchContent);
    }
}
