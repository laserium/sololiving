package com.sololiving.domain.article.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.article.exception.ArticleErrorCode;
import com.sololiving.domain.article.mapper.ArticleLikeMapper;
import com.sololiving.domain.article.mapper.ArticleMapper;
import com.sololiving.domain.article.vo.ArticleLikeVo;
import com.sololiving.domain.block.exception.BlockErrorCode;
import com.sololiving.domain.block.mapper.BlockMapper;
import com.sololiving.domain.log.enums.BoardMethod;
import com.sololiving.domain.log.service.UserActivityLogService;
import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.domain.user.mapper.UserViewMapper;
import com.sololiving.global.exception.GlobalErrorCode;
import com.sololiving.global.exception.error.ErrorException;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ArticleLikeService {

    private final ArticleLikeMapper articleLikeMapper;
    private final ArticleMapper articleMapper;
    private final UserViewMapper userViewMapper;
    private final BlockMapper blockMapper;
    private final UserActivityLogService userActivityLogService;

    // 게시글 추천
    public void likeArticle(Long articleId, String userId, String ipAddress) {
        validateLikeArticle(articleId, userId);
        insertLikeArticle(articleId, userId);

        // 사용자 행동 로그 처리
        userActivityLogService.insertArticleLog(userId, ipAddress, articleId, BoardMethod.LIKE);
    }

    private void validateLikeArticle(Long articleId, String userId) {
        // 입력값 NULL 검증
        if (articleId == null) {
            throw new ErrorException(GlobalErrorCode.REQUEST_IS_NULL);
        }
        // 게시글 존재 유무 확인
        if (!articleMapper.checkArticleExists(articleId)) {
            throw new ErrorException(ArticleErrorCode.ARTICLE_NOT_FOUND);
        }
        // 차단 유무 확인
        if (blockMapper.existsBlock(userId, articleMapper.selectWriterByArticleId(articleId))) {
            throw new ErrorException(BlockErrorCode.ALREADY_BLOCKED);
        }
        // 탈퇴한 회원의 게시글인지 확인
        if (userViewMapper.isUserDeleted(userId)) {
            throw new ErrorException(UserErrorCode.IS_DELETED_USER);
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
    public void likeArticleCancle(Long articleId, String userId, String ipAddress) {
        validateLikeArticleCancle(articleId, userId);
        insertLikeArticleCancle(articleId, userId);

        // 사용자 행동 로그 처리
        userActivityLogService.insertArticleLog(userId, ipAddress, articleId, BoardMethod.UNLIKE);
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
        if (!articleLikeMapper.hasUserLikedArticle(articleId, userId)) {
            throw new ErrorException(ArticleErrorCode.CANNOT_LIKE_DUPLICATE);
        }
    }

    @Transactional
    private void insertLikeArticleCancle(Long articleId, String userId) {
        articleLikeMapper.deleteArticleLike(articleId, userId);
        articleMapper.updateArticleLikeCount(articleId);
    }
}
