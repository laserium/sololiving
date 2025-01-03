package com.sololiving.domain.article.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sololiving.domain.article.dto.request.ArticleSearchRequestDto;
import com.sololiving.domain.article.dto.response.ViewAllArticlesListResponseDto;
import com.sololiving.domain.article.dto.response.ViewArticleDetailsResponseDto;
import com.sololiving.domain.article.dto.response.ViewArticlesListResponseDto;
import com.sololiving.domain.article.dto.response.ViewTopArticlesResponseDto;
import com.sololiving.domain.article.exception.ArticleErrorCode;
import com.sololiving.domain.article.mapper.ArticleMapper;
import com.sololiving.domain.article.mapper.ArticleViewMapper;
import com.sololiving.domain.article.util.TimeAgoUtil;
import com.sololiving.domain.log.enums.BoardMethod;
import com.sololiving.domain.log.service.UserActivityLogService;
import com.sololiving.domain.media.mapper.MediaMapper;
import com.sololiving.domain.user.exception.user_setting.UserSettingErrorCode;
import com.sololiving.domain.user.mapper.UserSettingMapper;
import com.sololiving.domain.user.service.UserAuthService;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.util.DecodeParameterUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ArticleViewService {

    private final ArticleService articleService;
    private final ArticleViewMapper articleViewMapper;
    private final UserAuthService userAuthService;
    private final MediaMapper mediaMapper;
    private final ArticleMapper articleMapper;
    private final UserSettingMapper userSettingMapper;
    private final UserActivityLogService userActivityLogService;

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
        List<ViewArticlesListResponseDto> articles = articleViewMapper.selectArticlesByCategoryId(
                decodeSearchParameters(requestDto));

        articles.forEach(article -> {
            String timeAgo = TimeAgoUtil.getTimeAgo(article.getCreatedAt());
            article.setTimeAgo(timeAgo);
        });

        return articles;
    }

    // 게시글 상세 조회 API
    public ViewArticleDetailsResponseDto viewArticleDetails(Long articleId, String userId, String ipAddress) {
        // 1. 캐시된 게시글 정보 가져오기
        ViewArticleDetailsResponseDto responseDto = setArticleDetails(articleId, userId);
        // 2. 조회수 증가
        articleService.incrementArticleViewCount(articleId);

        // 사용자 행동 로그 처리
        if (userAuthService.isUserIdAvailable(userId)) {
            userActivityLogService.insertArticleLog(ipAddress, ipAddress, articleId, BoardMethod.VIEW);
        } else {
            userActivityLogService.insertArticleLog(userId, ipAddress, articleId, BoardMethod.VIEW);
        }
        return responseDto;
    }

    // 게시글 상세 조회
    // @Cacheable(value = "articleDetails", key = "'ARTICLE_VIEW:DETAIL:' +
    // #articleId")
    private ViewArticleDetailsResponseDto setArticleDetails(Long articleId, String userId) {
        ViewArticleDetailsResponseDto responseDto = articleViewMapper.selectByArticleId(articleId, userId);
        if (responseDto == null || !articleMapper.checkArticleExists(articleId)) {
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
    public List<ViewArticlesListResponseDto> viewUserArticlesList(ArticleSearchRequestDto requestDto) {
        requestDto = decodeSearchParameters(requestDto);

        // 사용자 설정 필터
        if (!requestDto.getUserId().equals(requestDto.getWriter())
                && !userSettingMapper.isArticleSharingEnabled(requestDto.getWriter())) {
            throw new ErrorException(UserSettingErrorCode.ARTICLE_SHARING_DISABLED);
        }

        List<ViewArticlesListResponseDto> articles = articleViewMapper.selectUserArticles(requestDto);
        articles.forEach(article -> {
            String timeAgo = TimeAgoUtil.getTimeAgo(article.getCreatedAt());
            article.setTimeAgo(timeAgo);
        });

        return articles;
    }

    // 사용자가 추천한 게시글 목록 조회
    public List<ViewArticlesListResponseDto> viewUserLikeArticlesList(
            ArticleSearchRequestDto requestDto) {
        requestDto = decodeSearchParameters(requestDto);

        // 사용자 설정 필터
        if (!requestDto.getUserId().equals(requestDto.getWriter())
                && !userSettingMapper.isLikedSharingEnabled(requestDto.getWriter())) {
            throw new ErrorException(UserSettingErrorCode.LIKED_SHARING_DISABLED);
        }

        List<ViewArticlesListResponseDto> articles = articleViewMapper.selectUserLikeArticles(requestDto);
        articles.forEach(article -> {
            String timeAgo = TimeAgoUtil.getTimeAgo(article.getCreatedAt());
            article.setTimeAgo(timeAgo);
        });

        return articles;
    }

    private ArticleSearchRequestDto decodeSearchParameters(ArticleSearchRequestDto requestDto) {
        requestDto.setCategoryCode(DecodeParameterUtil.decodeSearchParameter(requestDto.getCategoryCode()));
        requestDto.setSearchTitle(DecodeParameterUtil.decodeSearchParameter(requestDto.getSearchTitle()));
        requestDto.setSearchContents(DecodeParameterUtil.decodeSearchParameter(requestDto.getSearchContents()));
        requestDto.setSearchWriter(DecodeParameterUtil.decodeSearchParameter(requestDto.getSearchWriter()));
        return requestDto;
    }

}
