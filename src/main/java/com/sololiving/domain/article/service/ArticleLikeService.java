package com.sololiving.domain.article.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.article.exception.ArticleErrorCode;
import com.sololiving.domain.article.mapper.ArticleLikeMapper;
import com.sololiving.domain.article.mapper.ArticleMapper;
import com.sololiving.domain.article.vo.ArticleLikeVo;
import com.sololiving.global.aop.CheckBlockedUser;
import com.sololiving.global.exception.error.ErrorException;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ArticleLikeService {

    private final ArticleLikeMapper articleLikeMapper;
    private final ArticleMapper articleMapper;

    // 게시글 추천
    @CheckBlockedUser
    public void likeArticle(Long articleId, String userId) {
        validateLikeArticle(articleId, userId);
        insertLikeArticle(articleId, userId);
    }

    private void validateLikeArticle(Long articleId, String userId) {
        if (!articleMapper.checkArticleExists(articleId)) {
            throw new ErrorException(ArticleErrorCode.ARTICLE_NOT_FOUND);
        }
        // 본인이 작성한 게시글에 추천 불가
        if (articleMapper.verifyArticleWriter(articleId, userId)) {
            throw new ErrorException(ArticleErrorCode.CANNOT_LIKE_MY_ARTICLE);
        }
        // 이미 추천한 경우 다시 추천 불가
        if (articleLikeMapper.hasUserLikedArticle(articleId, userId)) {
            throw new ErrorException(ArticleErrorCode.CANNOT_LIKE_DUPLICATE);
        }
    }

    @Transactional
    private void insertLikeArticle(Long articleId, String userId) {
        ArticleLikeVo articleLikeVo = ArticleLikeVo.builder()
                .articleId(articleId)
                .userId(userId)
                .build();
        articleLikeMapper.insertArticleLike(articleLikeVo);
        articleMapper.updateArticleLikeCount(articleId);
    }

    // 게시글 추천 취소
    @CheckBlockedUser
    public void likeArticleCancle(Long articleId, String userId) {
        validateLikeArticleCancle(articleId, userId);
        insertLikeArticleCancle(articleId, userId);
    }

    private void validateLikeArticleCancle(Long articleId, String userId) {
        if (!articleMapper.checkArticleExists(articleId)) {
            throw new ErrorException(ArticleErrorCode.ARTICLE_NOT_FOUND);
        }
        // 본인이 작성한 게시글에 추천 불가
        if (articleMapper.verifyArticleWriter(articleId, userId)) {
            throw new ErrorException(ArticleErrorCode.CANNOT_LIKE_MY_ARTICLE);
        }
        // 이미 추천한 경우 다시 추천 불가
        if (articleLikeMapper.hasUserLikedArticle(articleId, userId)) {
            throw new ErrorException(ArticleErrorCode.CANNOT_LIKE_DUPLICATE);
        }
    }

    @Transactional
    private void insertLikeArticleCancle(Long articleId, String userId) {
        articleLikeMapper.deleteArticleLike(articleId, userId);
        articleMapper.updateArticleLikeCount(articleId);
    }
}
