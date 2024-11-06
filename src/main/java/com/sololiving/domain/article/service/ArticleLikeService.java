package com.sololiving.domain.article.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.article.exception.ArticleErrorCode;
import com.sololiving.domain.article.mapper.ArticleLikeMapper;
import com.sololiving.domain.article.mapper.ArticleMapper;
import com.sololiving.domain.article.vo.ArticleLikeVo;
import com.sololiving.global.exception.error.ErrorException;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ArticleLikeService {

    private final ArticleLikeMapper articleLikeMapper;
    private final ArticleMapper articleMapper;

    // 게시글 추천
    @Transactional
    public void likeArticle(Long articleId, String userId) {
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
        ArticleLikeVo articleLikeVo = ArticleLikeVo.builder()
                .articleId(articleId)
                .userId(userId)
                .build();

        articleLikeMapper.insertArticleLike(articleLikeVo);
        articleMapper.updateArticleLikeCount(articleId);
    }

    // 게시글 추천 취소
    @Transactional
    public void likeArticleCancle(Long articleId, String userId) {
        if (!articleMapper.checkArticleExists(articleId)) {
            throw new ErrorException(ArticleErrorCode.ARTICLE_NOT_FOUND);
        }
        // 추천하지 않았는데 추천 취소할 경우
        if (!articleLikeMapper.hasUserLikedArticle(articleId, userId)) {
            throw new ErrorException(ArticleErrorCode.NOT_LIKED_ARTICLE);
        }
        articleLikeMapper.deleteArticleLike(articleId, userId);
        articleMapper.updateArticleLikeCount(articleId);
    }
}
