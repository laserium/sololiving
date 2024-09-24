package com.sololiving.domain.article.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sololiving.domain.article.dto.response.ViewArticleResponseICDto.ViewArticleDetailsResponseDto;
import com.sololiving.domain.article.dto.response.ViewArticleResponseICDto.ViewArticlesListResponseDto;
import com.sololiving.domain.article.dto.response.ViewArticleResponseICDto.ViewTopArticlesResponseDto;

@Mapper
public interface ArticleViewMapper {

    // 게시글 목록 조회
    List<ViewArticlesListResponseDto> findArticlesByCategoryId(
            @Param("categoryCode") String categoryCode,
            @Param("page") int page);

    // 게시글 상세 조회
    ViewArticleDetailsResponseDto findByArticleId(Long articleId);

    // 메인 페이지 : 일주일간 인기 게시글 TOP 5 조회
    List<ViewTopArticlesResponseDto> findPopularArticleListInMain();

    // 메인 페이지 : 대표 카테고리의 게시글 목록 조회
    List<ViewTopArticlesResponseDto> findArticlesListInMain(@Param("categoryCode") String categoryCode);
}
