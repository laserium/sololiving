package com.sololiving.domain.report.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sololiving.domain.report.dto.request.ViewReportRequestDto;
import com.sololiving.domain.report.dto.response.ViewReportListResponseDto;
import com.sololiving.domain.report.mapper.ReportViewMapper;
import com.sololiving.domain.user.mapper.UserAuthMapper;
import com.sololiving.global.exception.error.ErrorException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportViewService {

    private final ReportViewMapper reportViewMapper;

    public List<ViewReportListResponseDto> viewReportList(ViewReportRequestDto requestDto) {
        return reportViewMapper.selectReportList(requestDto);
    }

}
