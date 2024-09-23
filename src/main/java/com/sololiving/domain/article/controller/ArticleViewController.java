package com.sololiving.domain.article.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.article.dto.response.ViewArticleResponseICDto.ViewArticleDetailsResponseDto;
import com.sololiving.domain.article.dto.response.ViewArticleResponseICDto.ViewArticlesListResponseDto;
import com.sololiving.domain.article.service.ArticleViewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleViewController {

    private final ArticleViewService articleViewService;

    // 페이지 번호와 페이지 당 게시글 수를 전달받아 처리
    @GetMapping("/list")
    public ResponseEntity<List<ViewArticlesListResponseDto>> viewArticlesList(
            @RequestParam("categoryId") Long categoryId,
            @RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.status(HttpStatus.OK).body(articleViewService.viewArticlesList(categoryId, page));
    }

    // 게시글 상세 조회
    @GetMapping("/{articleId}")
    public ResponseEntity<ViewArticleDetailsResponseDto> viewArticleDetails(@PathVariable Long articleId) {
        return ResponseEntity.status(HttpStatus.OK).body(articleViewService.viewArticleDetails(articleId));
    }

    // 인기 게시글 조회
    @GetMapping("/top")
    public ResponseEntity<ViewArticleDetailsResponseDto> viewTopArticleList(@RequestParam String param) {
        return ResponseEntity.status(HttpStatus.OK).body(articleViewService.viewArticleDetails(articleId));
    }

}
