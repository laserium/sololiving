package com.sololiving.domain.article.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sololiving.domain.article.dto.request.ArticleSearchRequestDto;
import com.sololiving.domain.article.dto.response.ViewAllArticlesListResponseDto;
import com.sololiving.domain.article.dto.response.ViewArticleDetailsResponseDto;
import com.sololiving.domain.article.dto.response.ViewArticlesListResponseDto;
import com.sololiving.domain.article.dto.response.ViewTopArticlesResponseDto;

@Mapper
public interface ArticleViewMapper {

    // 게시글 전체 목록 조회
    List<ViewAllArticlesListResponseDto> selectAllArticlesList(
            @Param("userId") String userId,
            @Param("sort") String sort);

    // 게시글 목록 조회
    List<ViewArticlesListResponseDto> selectArticlesByCategoryId(
            ArticleSearchRequestDto requestDto);

    // 게시글 상세 조회
    ViewArticleDetailsResponseDto selectByArticleId(@Param("articleId") Long articleId, @Param("userId") String userId);

    // 메인페이지 인기 게시글 TOP 10
    List<ViewTopArticlesResponseDto> selectPopularArticles();

    // 사용자가 작성한 게시글 목록 조회
    List<ViewArticlesListResponseDto> selectUserArticles(ArticleSearchRequestDto requestDto);

    // 사용자가 추천한 게시글 목록 조회
    List<ViewArticlesListResponseDto> selectUserLikeArticles(ArticleSearchRequestDto requestDto);
}
