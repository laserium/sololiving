package com.sololiving.domain.article.controller;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.article.dto.request.ArticleSearchRequestDto;
import com.sololiving.domain.article.dto.response.ViewAllArticlesListResponseDto;
import com.sololiving.domain.article.dto.response.ViewArticleDetailsResponseDto;
import com.sololiving.domain.article.service.ArticleViewService;
import com.sololiving.domain.article.dto.response.ViewArticlesListResponseDto;
import com.sololiving.domain.article.dto.response.ViewTopArticlesResponseDto;
import com.sololiving.global.util.SecurityUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/articles")
public class ArticleViewController {

    private final ArticleViewService articleViewService;

    // 전체 게시글 목록 조회
    @GetMapping("/all")
    public ResponseEntity<List<ViewAllArticlesListResponseDto>> viewAllArticles(HttpServletRequest httpServletRequest,
            @RequestParam(defaultValue = "recent") String sort) {
        String userId = SecurityUtil.getCurrentUserId();
        return ResponseEntity.status(HttpStatus.OK).body(articleViewService.viewAllArticlesList(userId, sort));
    }

    // 게시글 목록 조회
    @GetMapping("/list")
    public ResponseEntity<List<ViewArticlesListResponseDto>> viewArticlesList(
            @ModelAttribute ArticleSearchRequestDto requestDto) {
        String userId = SecurityUtil.getCurrentUserId();
        requestDto.setUserId(userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(articleViewService.viewArticlesList(requestDto));
    }

    // 게시글 상세 조회
    @GetMapping("/{articleId}")
    public ResponseEntity<ViewArticleDetailsResponseDto> viewArticleDetails(@PathVariable Long articleId) {
        return ResponseEntity.status(HttpStatus.OK).body(articleViewService.viewArticleDetails(articleId));
    }

    // 메인 페이지 : 하루 기준 인기 게시글 TOP 10 조회
    @GetMapping("/popular")
    public ResponseEntity<List<ViewTopArticlesResponseDto>> viewPopularArticleListInMain() {
        return ResponseEntity.status(HttpStatus.OK).body(articleViewService.viewPopularArticleListInMain());
    }

    // 사용자가 작성한 게시글 목록 조회
    @GetMapping("/{writer}")
    public ResponseEntity<List<ViewArticlesListResponseDto>> viewUserArticlesList(@PathVariable String writer,
            @ModelAttribute ArticleSearchRequestDto requestDto, HttpServletRequest httpServletRequest) {

        String userId = SecurityUtil.getCurrentUserId();
        requestDto.setUserId(userId);

        // USER_SETTING 에 article_sharing_enabled 조건 추가
        return ResponseEntity.status(HttpStatus.OK)
                .body(articleViewService.viewUserArticlesList(writer, requestDto));
    }

    // 사용자가 추천한 게시글 목록 조회
    @GetMapping("/{writer}/like")
    public ResponseEntity<List<ViewArticlesListResponseDto>> viewUserLikeArticlesList(@PathVariable String writer,
            @ModelAttribute ArticleSearchRequestDto requestDto) {
        String userId = SecurityUtil.getCurrentUserId();
        requestDto.setUserId(userId);
        // USER_SETTING 에 liked_sharing_enabled 조건 추가
        return ResponseEntity.status(HttpStatus.OK)
                .body(articleViewService.viewUserLikeArticlesList(writer, requestDto));
    }

}
