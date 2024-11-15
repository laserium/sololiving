package com.sololiving.domain.article.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.article.dto.request.SearchArticleLogListRequestDto;
import com.sololiving.domain.article.dto.response.DailyArticleCountDto;
import com.sololiving.domain.article.dto.response.DailyCategoryArticleCountDto;
import com.sololiving.domain.article.dto.response.ViewArticleCountsResponseDto;
import com.sololiving.domain.article.dto.response.ViewArticleLogListResponseDto;
import com.sololiving.domain.article.service.AdminArticleViewService;
import com.sololiving.global.aop.admin.AdminOnly;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/article")
public class AdminArticleViewController {

    private final AdminArticleViewService adminArticleViewService;

    // 게시글 로그 목록 조회
    @AdminOnly
    @GetMapping("/list")
    public ResponseEntity<List<ViewArticleLogListResponseDto>> viewArticleListInAdmin(
            @ModelAttribute SearchArticleLogListRequestDto requestDto,
            HttpServletRequest httpServletRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(adminArticleViewService.viewArticleListInAdmin(requestDto));
    }

    // 전체 게시글 수와 오늘 작성된 게시글 수 조회
    @AdminOnly
    @GetMapping("/counts")
    public ResponseEntity<ViewArticleCountsResponseDto> viewArticleCountsInAdmin(
            HttpServletRequest httpServletRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(adminArticleViewService.viewArticleCountsInAdmin());
    }

    // 최근 30일 동안 날짜별 게시글 작성 수 조회
    @AdminOnly
    @GetMapping("/counts/daily")
    public ResponseEntity<List<DailyArticleCountDto>> viewDailyArticleCounts(HttpServletRequest httpServletRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(adminArticleViewService.viewDailyArticleCounts());
    }

    // 최근 30일 동안 날짜 및 카테고리별 게시글 작성 수 조회
    @AdminOnly
    @GetMapping("/counts/daily-by-category")
    public ResponseEntity<List<DailyCategoryArticleCountDto>> viewDailyArticleCountsByCategory(
            HttpServletRequest httpServletRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(adminArticleViewService.viewDailyArticleCountsByCategory());
    }
}
