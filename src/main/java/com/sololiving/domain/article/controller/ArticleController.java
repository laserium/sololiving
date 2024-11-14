package com.sololiving.domain.article.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.article.dto.request.CreateArticleRequestDto;
import com.sololiving.domain.article.dto.request.UpdateArticleRequestDto;
import com.sololiving.domain.article.dto.response.CreateArticleResponseDto;
import com.sololiving.domain.article.exception.ArticleSuccessCode;
import com.sololiving.domain.article.service.ArticleService;
import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.domain.user.service.UserAuthService;
import com.sololiving.global.exception.ResponseMessage;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.util.SecurityUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleController {

    private final UserAuthService userAuthService;
    private final ArticleService articleService;

    // 게시글 작성
    @PostMapping("/posting")
    public ResponseEntity<CreateArticleResponseDto> addArticle(@RequestBody CreateArticleRequestDto requestDto,
            HttpServletRequest httpServletRequest) {
        String userId = SecurityUtil.getCurrentUserId();
        if (userAuthService.isUserIdAvailable(userId)) {
            throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
        }
        List<String> tempMediaUrls = requestDto.getTempMediaUrls();
        return ResponseEntity.status(HttpStatus.OK)
                .body(articleService.addArticle(requestDto, userId, tempMediaUrls));
    }

    // 게시글 수정
    @PutMapping("/{articleId}")
    public ResponseEntity<?> modifyArticle(@PathVariable Long articleId,
            @RequestBody UpdateArticleRequestDto requestDto, HttpServletRequest httpServletRequest) {
        // 작성자(회원) 검증
        String userId = SecurityUtil.getCurrentUserId();
        articleService.validateWriter(articleId, userId);

        // 수정
        articleService.modifyArticle(requestDto, articleId, userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage.createSuccessResponse(ArticleSuccessCode.SUCCESS_TO_UPDATE_ARTICLE));
    }

    // 게시글 삭제
    @DeleteMapping("/{articleId}")
    public ResponseEntity<?> removeArticle(@PathVariable Long articleId, HttpServletRequest httpServletRequest) {
        // 작성자(회원) 검증
        String userId = SecurityUtil.getCurrentUserId();
        articleService.validateWriter(articleId, userId);
        articleService.removeArticle(articleId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage.createSuccessResponse(ArticleSuccessCode.SUCCESS_TO_DELETE_ARTICLE));
    }

    // AI 댓글 포함 게시글 작성
    @PostMapping("/posting/ai")
    public ResponseEntity<CreateArticleResponseDto> addArticleWithAI(@RequestBody CreateArticleRequestDto requestDto,
            HttpServletRequest httpServletRequest) {
        // 회원 유무 검증
        String userId = SecurityUtil.getCurrentUserId();
        if (userAuthService.isUserIdAvailable(userId)) {
            throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
        }
        List<String> tempMediaUrls = requestDto.getTempMediaUrls();

        CreateArticleResponseDto responseDto = articleService.addArticle(requestDto, userId, tempMediaUrls);

        // commentService.generateAIComment(responseDto.getArticleId());

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
