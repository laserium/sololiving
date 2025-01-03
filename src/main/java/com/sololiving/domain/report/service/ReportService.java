package com.sololiving.domain.report.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sololiving.domain.article.exception.ArticleErrorCode;
import com.sololiving.domain.article.mapper.ArticleMapper;
import com.sololiving.domain.comment.exception.CommentErrorCode;
import com.sololiving.domain.comment.mapper.CommentMapper;
import com.sololiving.domain.report.dto.request.ReportRequestDto;
import com.sololiving.domain.report.enums.ReportStatus;
import com.sololiving.domain.report.enums.ReportType;
import com.sololiving.domain.report.enums.SubjectType;
import com.sololiving.domain.report.exception.ReportErrorCode;
import com.sololiving.domain.report.mapper.ReportMapper;
import com.sololiving.domain.report.vo.ReportVo;
import com.sololiving.global.exception.GlobalErrorCode;
import com.sololiving.global.exception.error.ErrorException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportMapper reportMapper;
    private final ArticleMapper articleMapper;
    private final CommentMapper commentMapper;

    // 신고하기
    @Transactional
    public void addReport(ReportRequestDto requestDto, String userId) {
        validateAddReport(requestDto);
        ReportVo reportVo = buildAddReport(requestDto, userId);
        reportMapper.insertReport(reportVo);
        updateSubjectStatusToBlind(requestDto);
    }

    private ReportVo buildAddReport(ReportRequestDto requestDto, String userId) {
        ReportType reportType = requestDto.getReportType();
        if (reportType == ReportType.ABUSE ||
                reportType == ReportType.SPAM ||
                reportType == ReportType.SEXUAL_CONTENT ||
                reportType == ReportType.IMPERSONATION_OR_FRAUD) {
            return ReportVo.builder()
                    .reporter(userId)
                    .subjectType(requestDto.getSubjectType())
                    .subjectId(requestDto.getSubjectId())
                    .reportType(requestDto.getReportType())
                    .reportReason(requestDto.getReportReason())
                    .reportStatus(ReportStatus.RESOLVED)
                    .build();

        } else {
            return ReportVo.builder()
                    .reporter(userId)
                    .subjectType(requestDto.getSubjectType())
                    .subjectId(requestDto.getSubjectId())
                    .reportType(requestDto.getReportType())
                    .reportReason(requestDto.getReportReason())
                    .reportStatus(ReportStatus.PENDING)
                    .build();
        }
    }

    private void validateAddReport(ReportRequestDto requestDto) {
        SubjectType subjectType = requestDto.getSubjectType();
        Long subjectId = requestDto.getSubjectId();
        ReportType reportType = requestDto.getReportType();

        if (subjectType == null) {
            throw new ErrorException(GlobalErrorCode.REQUEST_IS_NULL);
        }
        if (subjectId == null) {
            throw new ErrorException(GlobalErrorCode.REQUEST_IS_NULL);
        }
        if (reportType == null) {
            throw new ErrorException(GlobalErrorCode.REQUEST_IS_NULL);
        }
        if (subjectType == SubjectType.ARTICLE
                && !articleMapper.checkArticleExists(requestDto.getSubjectId())) {
            throw new ErrorException(ArticleErrorCode.ARTICLE_NOT_FOUND);
        } else if ((subjectType == SubjectType.COMMENT
                && !commentMapper.checkComment(requestDto.getSubjectId()))) {
            throw new ErrorException(CommentErrorCode.NOT_FOUND_COMMENT);
        }
    }

    // 컨텐츠 자동 블라인드
    private void updateSubjectStatusToBlind(ReportRequestDto requestDto) {
        SubjectType subjectType = requestDto.getSubjectType();
        Long subjectId = requestDto.getSubjectId();
        int countReport = reportMapper.countReport(subjectId);
        if (countReport >= 10 && subjectType == SubjectType.ARTICLE) {
            articleMapper.updateArticleStatusToBlind(subjectId);
        }
        if (countReport >= 10 && subjectType == SubjectType.COMMENT) {
            commentMapper.updateCommentStatusToBlind(subjectId);
        }
    }

    // 신고 기록 삭제
    public void removeReport(String userId, Long reportId) {
        if (!reportMapper.existsReport(reportId)) {
            throw new ErrorException(ReportErrorCode.REPORT_NOT_FOUND);
        }
        deleteReport(reportId);
    }

    @Transactional
    private void deleteReport(Long reportId) {
        reportMapper.deleteReport(reportId);
    }

    // 신고 상태 수정
    public void modifyReportStatus(Long reportId, ReportStatus reportStatus) {
        validateModifyReportStatus(reportId, reportStatus);
        updateReportStatus(reportId, reportStatus);
    }

    private void validateModifyReportStatus(Long reportId, ReportStatus reportStatus) {
        if (!reportMapper.existsReport(reportId)) {
            throw new ErrorException(ReportErrorCode.REPORT_NOT_FOUND);
        }
        if (reportStatus != ReportStatus.PENDING && reportStatus != ReportStatus.REJECTED
                && reportStatus != ReportStatus.RESOLVED) {
            throw new ErrorException(ReportErrorCode.REPORT_INPUT_INVALID);
        }
        if (reportMapper.selectReportStatusByReportId(reportId) != ReportStatus.PENDING) {
            throw new ErrorException(ReportErrorCode.REPORT_ALREADY_UPDATED);
        }
    }

    @Transactional
    private void updateReportStatus(Long reportId, ReportStatus reportStatus) {
        reportMapper.updateReportStatus(reportId, reportStatus);

    }
}
