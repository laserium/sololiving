package com.sololiving.domain.article.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.article.dto.request.CreateArticleRequestDto;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleController {

    // 게시글 작성
    @PostMapping("/")
    public ResponseEntity<?> createArticle(@RequestBody CreateArticleRequestDto requestDto) {
        // 회원 유무 검증
        // 회원 아이디 find
        // create
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
