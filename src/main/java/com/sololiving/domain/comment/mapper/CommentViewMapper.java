package com.sololiving.domain.comment.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sololiving.domain.comment.dto.response.ViewCommentsResponseDto;
import com.sololiving.domain.comment.dto.response.ViewUsersCommentsResponseDto;

@Mapper
public interface CommentViewMapper {
    // 게시물의 댓글 조회
    List<ViewCommentsResponseDto> selectAllComments(@Param("articleId") Long articleId,
            @Param("userId") String userId);

    // 사용자의 댓글 조회
    List<ViewUsersCommentsResponseDto> selectUserComments(
            @Param("writer") String writer,
            @Param("userId") String userId,
            @Param("searchContent") String searchContent);
}
