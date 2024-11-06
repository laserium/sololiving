package com.sololiving.domain.comment.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sololiving.domain.comment.dto.response.ViewCommentsResponseDto;

@Mapper
public interface CommentViewMapper {
    List<ViewCommentsResponseDto> selectAllComments(@Param("articleId") Long articleId);
}
