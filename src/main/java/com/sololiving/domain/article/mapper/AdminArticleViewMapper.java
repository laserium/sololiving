package com.sololiving.domain.article.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.sololiving.domain.article.dto.request.SearchArticleLogListRequestDto;
import com.sololiving.domain.article.dto.response.DailyArticleCountDto;
import com.sololiving.domain.article.dto.response.DailyCategoryArticleCountDto;
import com.sololiving.domain.article.dto.response.ViewArticleCountsResponseDto;
import com.sololiving.domain.article.dto.response.ViewArticleLogListResponseDto;

@Mapper
public interface AdminArticleViewMapper {

    // 게시글 로그 목록 조회
    List<ViewArticleLogListResponseDto> selectArticleLogs(SearchArticleLogListRequestDto requestDto);

    // 전체 게시글 수와 오늘 작성된 게시글 수 조회
    ViewArticleCountsResponseDto selectArticleCounts();

    // 최근 30일 동안 날짜별 게시글 작성 수 조회
    List<DailyArticleCountDto> selectDailyArticleCounts();

    // 최근 30일 동안 날짜 및 카테고리별 게시글 작성 수 조회
    List<DailyCategoryArticleCountDto> selectDailyArticleCountsByCategory();
}
