package com.sololiving.domain.report.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.sololiving.domain.report.dto.request.ViewReportRequestDto;
import com.sololiving.domain.report.dto.response.ViewReportCountsResponseDto;
import com.sololiving.domain.report.dto.response.ViewReportListResponseDto;

@Mapper
public interface ReportViewMapper {
    List<ViewReportListResponseDto> selectReportList(ViewReportRequestDto requestDto);

    ViewReportCountsResponseDto selectReportCounts();

}
