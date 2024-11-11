package com.sololiving.domain.article.service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sololiving.domain.article.dto.request.ArticleSearchRequestDto;
import com.sololiving.domain.article.dto.response.ViewAllArticlesListResponseDto;
import com.sololiving.domain.article.dto.response.ViewArticleDetailsResponseDto;
import com.sololiving.domain.article.dto.response.ViewArticlesListResponseDto;
import com.sololiving.domain.article.dto.response.ViewTopArticlesResponseDto;
import com.sololiving.domain.article.exception.ArticleErrorCode;
import com.sololiving.domain.article.mapper.ArticleViewMapper;
import com.sololiving.domain.article.util.TimeAgoUtil;
import com.sololiving.domain.media.mapper.MediaMapper;
import com.sololiving.global.exception.error.ErrorException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ArticleViewService {

    private final ArticleService articleService;
    private final ArticleViewMapper articleViewMapper;
    private final MediaMapper mediaMapper;

    // 전체 게시글 조회
    public List<ViewAllArticlesListResponseDto> viewAllArticlesList(String userId, String sort) {
        List<ViewAllArticlesListResponseDto> articles = articleViewMapper.selectAllArticlesList(userId, sort);

        articles.forEach(article -> {
            String timeAgo = TimeAgoUtil.getTimeAgo(article.getCreatedAt());
            article.setTimeAgo(timeAgo);
        });

        return articles;
    }

    // 게시글 목록 조회
    public List<ViewArticlesListResponseDto> viewArticlesList(ArticleSearchRequestDto requestDto) {

        decodeSearchParameters(requestDto);

        List<ViewArticlesListResponseDto> articles = articleViewMapper.selectArticlesByCategoryId(
                requestDto);

        articles.forEach(article -> {
            String timeAgo = TimeAgoUtil.getTimeAgo(article.getCreatedAt());
            article.setTimeAgo(timeAgo);
        });

        return articles;
    }

    // 게시글 상세 조회 API
    public ViewArticleDetailsResponseDto viewArticleDetails(Long articleId) {
        // 1. 캐시된 게시글 정보 가져오기
        ViewArticleDetailsResponseDto responseDto = setArticleDetails(articleId);
        // 2. 조회수 증가
        articleService.incrementArticleViewCount(articleId);
        return responseDto;
    }

    // 게시글 상세 조회
    // @Cacheable(value = "articleDetails", key = "'ARTICLE_VIEW:DETAIL:' +
    // #articleId")
    private ViewArticleDetailsResponseDto setArticleDetails(Long articleId) {
        ViewArticleDetailsResponseDto responseDto = articleViewMapper.selectByArticleId(articleId);
        if (responseDto == null) {
            throw new ErrorException(ArticleErrorCode.ARTICLE_NOT_FOUND);
        }
        responseDto.setMediaList(mediaMapper.selectByArticleId(articleId));
        responseDto.setTimeAgo(TimeAgoUtil.getTimeAgo(responseDto.getCreatedAt()));
        return responseDto;
    }

    // 캐싱 예정
    // 메인 페이지 : 하루 기준 인기 게시글 TOP 10 조회
    public List<ViewTopArticlesResponseDto> viewPopularArticleListInMain() {
        List<ViewTopArticlesResponseDto> articles = articleViewMapper.selectPopularArticles();

        // 시간 정보 설정 (timeAgo)
        articles.forEach(article -> {
            String timeAgo = TimeAgoUtil.getTimeAgo(article.getCreatedAt());
            article.setTimeAgo(timeAgo);
        });

        return articles;
    }

    // 사용자가 작성한 게시글 목록 조회
    public List<ViewArticlesListResponseDto> viewUserArticlesList(String writer, ArticleSearchRequestDto requestDto) {
        List<ViewArticlesListResponseDto> articles = articleViewMapper.selectUserArticles(writer,
                requestDto);
        decodeSearchParameters(requestDto);
        articles.forEach(article -> {
            String timeAgo = TimeAgoUtil.getTimeAgo(article.getCreatedAt());
            article.setTimeAgo(timeAgo);
        });

        return articles;
    }

    // 사용자가 추천한 게시글 목록 조회
    public List<ViewArticlesListResponseDto> viewUserLikeArticlesList(String writer,
            ArticleSearchRequestDto requestDto) {
        List<ViewArticlesListResponseDto> articles = articleViewMapper.selectUserLikeArticles(
                writer, requestDto);
        decodeSearchParameters(requestDto);
        articles.forEach(article -> {
            String timeAgo = TimeAgoUtil.getTimeAgo(article.getCreatedAt());
            article.setTimeAgo(timeAgo);
        });

        return articles;
    }

    private void decodeSearchParameters(ArticleSearchRequestDto requestDto) {
        if (requestDto.getSearchTitle() != null) {
            requestDto.setSearchTitle(URLDecoder.decode(requestDto.getSearchTitle(), StandardCharsets.UTF_8));
        }
        if (requestDto.getSearchContents() != null) {
            requestDto.setSearchContents(URLDecoder.decode(requestDto.getSearchContents(), StandardCharsets.UTF_8));
        }
        if (requestDto.getSearchWriter() != null) {
            requestDto.setSearchWriter(URLDecoder.decode(requestDto.getSearchWriter(), StandardCharsets.UTF_8));
        }
    }

}
