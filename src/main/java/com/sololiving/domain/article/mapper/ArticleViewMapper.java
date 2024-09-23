package com.sololiving.domain.article.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sololiving.domain.article.dto.response.ViewArticleResponseICDto.ViewArticleDetailsResponseDto;
import com.sololiving.domain.article.dto.response.ViewArticleResponseICDto.ViewArticlesListResponseDto;

@Mapper
public interface ArticleViewMapper {
    List<ViewArticlesListResponseDto> findArticlesByCategoryId(
            @Param("categoryId") Long categoryId,
            @Param("page") int page);

    ViewArticleDetailsResponseDto findByArticleId(Long articleId);

}
