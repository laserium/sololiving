package com.sololiving.domain.report.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sololiving.domain.report.dto.request.ViewReportRequestDto;
import com.sololiving.domain.report.dto.response.ViewReportCountsResponseDto;
import com.sololiving.domain.report.dto.response.ViewReportListResponseDto;
import com.sololiving.domain.report.mapper.ReportViewMapper;
import com.sololiving.global.util.DecodeParameterUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportViewService {

    private final ReportViewMapper reportViewMapper;

    // 신고 목록 조회
    public List<ViewReportListResponseDto> viewReportList(ViewReportRequestDto requestDto) {
        DecodeParameterUtil.decodeSearchParameter(requestDto.getSearchUserId());
        return reportViewMapper.selectReportList(requestDto);
    }

    // 신고 개수 조회
    public ViewReportCountsResponseDto viewReportCounts() {
        return reportViewMapper.selectReportCounts();
    }
}
