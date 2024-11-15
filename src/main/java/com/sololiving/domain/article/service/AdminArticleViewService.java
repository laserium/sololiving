package com.sololiving.domain.article.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.sololiving.domain.article.dto.request.SearchArticleLogListRequestDto;
import com.sololiving.domain.article.dto.response.DailyArticleCountDto;
import com.sololiving.domain.article.dto.response.DailyCategoryArticleCountDto;
import com.sololiving.domain.article.dto.response.ViewArticleCountsResponseDto;
import com.sololiving.domain.article.dto.response.ViewArticleLogListResponseDto;
import com.sololiving.domain.article.mapper.AdminArticleViewMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminArticleViewService {

    private final AdminArticleViewMapper adminArticleViewMapper;

    // 게시글 로그 목록 조회
    public List<ViewArticleLogListResponseDto> viewArticleListInAdmin(SearchArticleLogListRequestDto requestDto) {
        return adminArticleViewMapper.selectArticleLogs(requestDto);

    }

    // 전체 게시글 수와 오늘 작성된 게시글 수 조회
    public ViewArticleCountsResponseDto viewArticleCountsInAdmin() {
        return adminArticleViewMapper.selectArticleCounts();
    }

    // 최근 30일 동안 날짜별 게시글 작성 수 조회
    public List<DailyArticleCountDto> viewDailyArticleCounts() {
        return adminArticleViewMapper.selectDailyArticleCounts();
    }

    // 최근 30일 동안 날짜 및 카테고리별 게시글 작성 수 조회
    public List<DailyCategoryArticleCountDto> viewDailyArticleCountsByCategory() {
        return adminArticleViewMapper.selectDailyArticleCountsByCategory();
    }

}
