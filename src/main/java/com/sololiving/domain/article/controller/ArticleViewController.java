package com.sololiving.domain.article.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.article.dto.response.ViewAllArticlesListResponseDto;
import com.sololiving.domain.article.dto.response.ViewArticleResponseICDto.ViewArticleDetailsResponseDto;
import com.sololiving.domain.article.dto.response.ViewArticleResponseICDto.ViewCategoryArticlesResponseDto;
import com.sololiving.domain.article.dto.response.ViewArticleResponseICDto.ViewTopArticlesResponseDto;
import com.sololiving.domain.article.dto.response.ViewArticlesListResponseDto;
import com.sololiving.domain.article.service.ArticleViewService;
import com.sololiving.global.util.SecurityUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/article")
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
    @GetMapping("/list/{categoryCode}")
    public ResponseEntity<List<ViewArticlesListResponseDto>> viewArticlesList(
            @PathVariable("categoryCode") String categoryCode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "recent") String sort) {
        String userId = SecurityUtil.getCurrentUserId();
        return ResponseEntity.status(HttpStatus.OK)
                .body(articleViewService.viewArticlesList(categoryCode, page, userId, sort));
    }

    // 게시글 상세 조회
    @GetMapping("/{articleId}")
    public ResponseEntity<ViewArticleDetailsResponseDto> viewArticleDetails(@PathVariable Long articleId) {
        return ResponseEntity.status(HttpStatus.OK).body(articleViewService.viewArticleDetails(articleId));
    }

    // 메인 페이지 : 일주일간 인기 게시글 TOP 5 조회
    @GetMapping("/main/popular")
    public ResponseEntity<List<ViewTopArticlesResponseDto>> viewPopularArticleListInMain() {
        return ResponseEntity.status(HttpStatus.OK).body(articleViewService.viewPopularArticleListInMain());
    }

    // 메인 페이지 : 대표 카테고리의 게시글 목록 조회
    @GetMapping("/main/{categoryCode}")
    public ResponseEntity<List<ViewCategoryArticlesResponseDto>> viewArticlesListInMain(
            @PathVariable("categoryCode") String categoryCode) {
        return ResponseEntity.status(HttpStatus.OK).body(articleViewService.viewArticlesListInMain(categoryCode));
    }

}
