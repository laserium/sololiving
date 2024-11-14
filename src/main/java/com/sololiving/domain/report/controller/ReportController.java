package com.sololiving.domain.report.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.report.dto.request.ReportRequestDto;
import com.sololiving.domain.report.enums.ReportStatus;
import com.sololiving.domain.report.exception.ReportSuccessCode;
import com.sololiving.domain.report.service.ReportService;
import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.domain.user.service.UserAuthService;
import com.sololiving.global.aop.admin.AdminOnly;
import com.sololiving.global.exception.ResponseMessage;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.exception.success.SuccessResponse;
import com.sololiving.global.util.SecurityUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;
    private final UserAuthService userAuthService;

    // 신고 하기
    @PostMapping("")
    public ResponseEntity<SuccessResponse> createReport(@RequestBody ReportRequestDto reportRequestDto,
            HttpServletRequest httpServletRequest) {
        String userId = SecurityUtil.getCurrentUserId();
        if (userAuthService.isUserIdAvailable(userId)) {
            throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
        }
        reportService.addReport(reportRequestDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseMessage.createSuccessResponse(ReportSuccessCode.REPORT_SUCCESS));
    }

    // 신고 기록 삭제
    @AdminOnly
    @DeleteMapping("/{reportId}")
    public ResponseEntity<SuccessResponse> removeReport(@PathVariable Long reportId,
            HttpServletRequest httpServletRequest) {
        String userId = SecurityUtil.getCurrentUserId();
        if (userAuthService.isUserIdAvailable(userId)) {
            throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
        }
        reportService.removeReport(userId, reportId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage.createSuccessResponse(ReportSuccessCode.SUCCESS_TO_DELETE_REPORT));
    }

    // 신고 상태 수정
    @AdminOnly
    @PatchMapping("/{reportId}/{reportStatus}")
    public ResponseEntity<SuccessResponse> updateReportStatus(@PathVariable Long reportId,
            @PathVariable ReportStatus reportStatus) {
        String userId = SecurityUtil.getCurrentUserId();
        if (userAuthService.isUserIdAvailable(userId)) {
            throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
        }
        reportService.modifyReportStatus(reportId, reportStatus);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage.createSuccessResponse(ReportSuccessCode.SUCCESS_TO_UPDATE_REPORT));
    }
}
