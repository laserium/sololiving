package com.sololiving.domain.report.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.report.dto.request.ViewReportRequestDto;
import com.sololiving.domain.report.dto.response.ViewReportListResponseDto;
import com.sololiving.domain.report.service.ReportService;
import com.sololiving.domain.report.service.ReportViewService;
import com.sololiving.global.aop.admin.AdminOnly;
import com.sololiving.global.util.SecurityUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportViewController {

    private final ReportViewService reportViewService;

    // [관리자] 신고 목록 조회
    @AdminOnly
    @GetMapping("/list")
    public ResponseEntity<List<ViewReportListResponseDto>> viewReportList(
            @ModelAttribute ViewReportRequestDto requestDto, HttpServletRequest httpServletRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(reportViewService.viewReportList(requestDto));
    }

}
