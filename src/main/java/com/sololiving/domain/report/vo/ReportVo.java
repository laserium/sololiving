package com.sololiving.domain.report.vo;

import java.time.LocalDateTime;

import com.sololiving.domain.report.enums.ReportStatus;
import com.sololiving.domain.report.enums.ReportType;
import com.sololiving.domain.report.enums.SubjectType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReportVo {

    private Long reportId;
    private String reporter;
    private SubjectType subjectType;
    private Long subjectId;
    private ReportType reportType;
    private String reportReason;
    private ReportStatus reportStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
