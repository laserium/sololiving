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
import com.sololiving.domain.media.mapper.MediaMapper;
import com.sololiving.global.exception.error.ErrorException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleViewService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ArticleViewMapper articleViewMapper;
    private final MediaMapper mediaMapper;

    // 게시글 목록 조회
    // @Cacheable(value = "articleList", key = "'ARTICLE_VIEW:LIST:' + #categoryCode
    // + ':' + #page")
    public List<ViewArticlesListResponseDto> viewArticlesList(String categoryCode, int page) {

        List<ViewArticlesListResponseDto> articles = articleViewMapper.findArticlesByCategoryId(categoryCode, page);

        articles.forEach(article -> {
            String timeAgo = TimeAgoUtil.getTimeAgo(article.getCreatedAt());
            article.setTimeAgo(timeAgo);

            boolean hasMedia = checkIfArticleHasMedia(article.getArticleId());
            article.setHasMedia(hasMedia);
        });

        return articles;
    }

    // 게시글 상세 조회 API
    public ViewArticleDetailsResponseDto viewArticleDetails(Long articleId) {
        // 1. 캐시된 게시글 정보 가져오기
        ViewArticleDetailsResponseDto responseDto = getCachedArticleDetails(articleId);
        // 2. 조회수 증가
        incrementArticleViewCount(articleId);
        return responseDto;
    }

    // 게시글 상세 조회
    // @Cacheable(value = "articleDetails", key = "'ARTICLE_VIEW:DETAIL:' +
    // #articleId")
    private ViewArticleDetailsResponseDto getCachedArticleDetails(Long articleId) {
        ViewArticleDetailsResponseDto responseDto = articleViewMapper.findByArticleId(articleId);
        if (responseDto == null) {
            throw new ErrorException(ArticleErrorCode.ARTICLE_NOT_FOUND);
        }
        responseDto.setTimeAgo(TimeAgoUtil.getTimeAgo(responseDto.getCreatedAt()));
        return responseDto;
    }

    // 게시글 조회수 증가
    private void incrementArticleViewCount(Long articleId) {
        redisTemplate.opsForValue().increment("ARTICLE:" + articleId + ":view_cnt");
    }

    // 메인 페이지 : 일주일간 인기 게시글 TOP 5 조회
    // @Cacheable(value = "popularArticles", key = "'ARTICLE_VIEW:MAIN:POPULAR'")
    public List<ViewTopArticlesResponseDto> viewPopularArticleListInMain() {
        List<ViewTopArticlesResponseDto> articles = articleViewMapper.findPopularArticleListInMain();
        articles.forEach(article -> {
            String timeAgo = TimeAgoUtil.getTimeAgo(article.getCreatedAt());
            article.setTimeAgo(timeAgo);
        });
        return articles;
    }

    // 메인 페이지 : 대표 카테고리의 게시글 목록 조회
    // @Cacheable(value = "mainCategoryArticles", key = "'ARTICLE_VIEW:MAIN:' +
    // #categoryCode")
    public List<ViewTopArticlesResponseDto> viewArticlesListInMain(String categoryCode) {
        List<ViewTopArticlesResponseDto> articles = articleViewMapper.findArticlesListInMain(categoryCode);
        articles.forEach(article -> {
            String timeAgo = TimeAgoUtil.getTimeAgo(article.getCreatedAt());
            article.setTimeAgo(timeAgo);
        });
        return articles;
    }

    private boolean checkIfArticleHasMedia(Long articleId) {
        // 미디어 테이블에서 articleId로 미디어가 있는지 확인하는 로직
        return mediaMapper.existsByArticleId(articleId); // 예: 미디어가 존재하면 true 반환
    }

}
