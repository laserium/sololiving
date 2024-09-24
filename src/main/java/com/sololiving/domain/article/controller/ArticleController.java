package com.sololiving.domain.article.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.article.dto.request.CreateArticleRequestDto;
import com.sololiving.domain.article.dto.request.UpdateArticleRequestDto;
import com.sololiving.domain.article.exception.ArticleSuccessCode;
import com.sololiving.domain.article.service.ArticleService;
import com.sololiving.domain.auth.exception.auth.AuthErrorCode;
import com.sololiving.domain.category.vo.CategoryVo;
import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.domain.user.service.UserAuthService;
import com.sololiving.global.exception.ResponseMessage;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.security.jwt.service.TokenProvider;
import com.sololiving.global.util.CookieService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleController {

    private final TokenProvider tokenProvider;
    private final CookieService cookieService;
    private final UserAuthService userAuthService;
    private final ArticleService articleService;

    // 게시글 작성
    @PostMapping("/posting")
    public ResponseEntity<?> createArticle(@RequestBody CreateArticleRequestDto requestDto,
            HttpServletRequest httpServletRequest) {
        // 회원 유무 검증
        String userId = tokenProvider.getUserId(cookieService.extractAccessTokenFromCookie(httpServletRequest));
        if (userAuthService.isUserIdAvailable(userId)) {
            throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
        }
        // 미디어 파일 처리: 클라이언트로부터 임시로 업로드된 파일 URL 리스트를 받는다고 가정
        List<String> tempMediaUrls = requestDto.getTempMediaUrls(); // 임시 미디어 파일 URL들
        // ArticleService에서 게시글 생성 및 미디어 파일 처리
        articleService.createArticle(requestDto, userId, tempMediaUrls);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage.createSuccessResponse(ArticleSuccessCode.SUCCESS_TO_POST_ARTICLE));
    }

    @PutMapping("/{articleId}")
    public ResponseEntity<?> updateArticle(@PathVariable Long articleId,
            @RequestBody UpdateArticleRequestDto requestDto) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(null);
    }
}
