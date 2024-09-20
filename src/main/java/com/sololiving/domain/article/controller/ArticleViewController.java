package com.sololiving.domain.article.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.article.service.ArticleViewService;
import com.sololiving.domain.article.vo.ArticleVo;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/articles")
public class ArticleViewController {

    private final ArticleViewService articleViewService;

    // 페이지 번호와 페이지 당 게시글 수를 전달받아 처리
    @GetMapping("")
    public ResponseEntity<List<ArticleVo>> getArticlesByScroll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        List<ArticleVo> articles = articleViewService.getArticlesByScroll(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(articles);
    }
}
