package com.sololiving.domain.article.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.sololiving.domain.article.dto.response.ViewArticleResponseICDto.ViewArticleDetailsResponseDto;
import com.sololiving.domain.article.dto.response.ViewArticleResponseICDto.ViewArticlesListResponseDto;
import com.sololiving.domain.article.dto.response.ViewArticleResponseICDto.ViewTopArticlesResponseDto;
import com.sololiving.domain.article.exception.ArticleErrorCode;
import com.sololiving.domain.article.mapper.ArticleViewMapper;
import com.sololiving.domain.article.util.TimeAgoUtil;
import com.sololiving.global.exception.error.ErrorException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleViewService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ArticleViewMapper articleViewMapper;

    // 게시글 목록 조회
    @Cacheable(value = "articleList", key = "'ARTICLE_VIEW:LIST:' + #categoryCode + ':' + #page")
    public List<ViewArticlesListResponseDto> viewArticlesList(String categoryCode, int page) {

        List<ViewArticlesListResponseDto> articles = articleViewMapper.findArticlesByCategoryId(categoryCode, page);

        articles.forEach(article -> {
            String timeAgo = TimeAgoUtil.getTimeAgo(article.getCreatedAt());
            article.setTimeAgo(timeAgo);
        });

        return articles;
    }

    // 게시글 상세 조회
    // @Cacheable(value = "articleDetails", key = "'ARTICLE_VIEW:DETAIL:' +
    // #articleId")
    public ViewArticleDetailsResponseDto viewArticleDetails(Long articleId) {
        ViewArticleDetailsResponseDto responseDto = articleViewMapper.findByArticleId(articleId);
        if (responseDto == null) {
            throw new ErrorException(ArticleErrorCode.ARTICLE_NOT_FOUND);
        }
        responseDto.setTimeAgo(TimeAgoUtil.getTimeAgo(responseDto.getCreatedAt()));

        // redis 에 임시 조회 수 저장
        redisTemplate.opsForValue().increment("ARTICLE:" + articleId + ":view_cnt");
        return responseDto;
    }

    // 메인 페이지 : 일주일간 인기 게시글 TOP 5 조회
    @Cacheable(value = "popularArticles", key = "'ARTICLE_VIEW:MAIN:POPULAR'")
    public List<ViewTopArticlesResponseDto> viewPopularArticleListInMain() {
        List<ViewTopArticlesResponseDto> articles = articleViewMapper.findPopularArticleListInMain();
        articles.forEach(article -> {
            String timeAgo = TimeAgoUtil.getTimeAgo(article.getCreatedAt());
            article.setTimeAgo(timeAgo);
        });
        return articles;
    }

    // 메인 페이지 : 대표 카테고리의 게시글 목록 조회
    @Cacheable(value = "mainCategoryArticles", key = "'ARTICLE_VIEW:MAIN:' + #categoryCode")
    public List<ViewTopArticlesResponseDto> viewArticlesListInMain(String categoryCode) {
        List<ViewTopArticlesResponseDto> articles = articleViewMapper.findArticlesListInMain(categoryCode);
        articles.forEach(article -> {
            String timeAgo = TimeAgoUtil.getTimeAgo(article.getCreatedAt());
            article.setTimeAgo(timeAgo);
        });
        return articles;
    }

}
