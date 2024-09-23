package com.sololiving.domain.article.service;

import java.util.List;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.sololiving.domain.article.dto.response.ViewArticleResponseICDto.ViewArticleDetailsResponseDto;
import com.sololiving.domain.article.dto.response.ViewArticleResponseICDto.ViewArticlesListResponseDto;
import com.sololiving.domain.article.exception.ArticleErrorCode;
import com.sololiving.domain.article.mapper.ArticleViewMapper;
import com.sololiving.domain.article.util.TimeAgoUtil;
import com.sololiving.global.exception.error.ErrorException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleViewService {

    private final ArticleViewMapper articleViewMapper;

    // 게시글 목록 조회
    public List<ViewArticlesListResponseDto> viewArticlesList(Long categoryId, int page) {
        List<ViewArticlesListResponseDto> articles = articleViewMapper.findArticlesByCategoryId(categoryId, page);

        articles.forEach(article -> {
            String timeAgo = TimeAgoUtil.getTimeAgo(article.getCreatedAt());
            article.setTimeAgo(timeAgo);
        });

        return articles;
    }

    // 게시글 상세 조회
    public ViewArticleDetailsResponseDto viewArticleDetails(Long articleId) {
        ViewArticleDetailsResponseDto responseDto = articleViewMapper.findByArticleId(articleId);
        if (responseDto == null) {
            throw new ErrorException(ArticleErrorCode.ARTICLE_NOT_FOUND);
        }
        responseDto.setTimeAgo(TimeAgoUtil.getTimeAgo(responseDto.getCreatedAt()));
        return responseDto;
    }

}
