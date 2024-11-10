package com.sololiving.domain.article.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.article.exception.ArticleSuccessCode;
import com.sololiving.domain.article.service.ArticleLikeService;
import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.domain.user.service.UserAuthService;
import com.sololiving.global.exception.ResponseMessage;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.exception.success.SuccessResponse;
import com.sololiving.global.util.SecurityUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleLikeController {

    private final UserAuthService userAuthService;
    private final ArticleLikeService articleLikeService;

    // 게시글 추천
    @PostMapping("/{articleId}/like")
    public ResponseEntity<SuccessResponse> likeArticle(@PathVariable Long articleId,
            HttpServletRequest httpServletRequest) {
        String userId = SecurityUtil.getCurrentUserId();
        if (userAuthService.isUserIdAvailable(userId)) {
            throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
        }
        articleLikeService.likeArticle(articleId, userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage.createSuccessResponse(ArticleSuccessCode.SUCCESS_TO_LIKE_ARTICLE));
    }

    // 게시글 추천 취소
    @DeleteMapping("{articleId}/like")
    public ResponseEntity<SuccessResponse> likeArticleCancle(@PathVariable Long articleId,
            HttpServletRequest httpServletRequest) {
        String userId = SecurityUtil.getCurrentUserId();
        if (userAuthService.isUserIdAvailable(userId)) {
            throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
        }
        articleLikeService.likeArticleCancle(articleId, userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage.createSuccessResponse(ArticleSuccessCode.SUCCESS_TO_CANCLE_LIKE_ARTICLE));
    }

}
