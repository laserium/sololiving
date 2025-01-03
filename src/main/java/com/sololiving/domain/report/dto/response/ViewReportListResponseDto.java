package com.sololiving.domain.report.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sololiving.domain.report.enums.ReportStatus;
import com.sololiving.domain.report.enums.ReportType;
import com.sololiving.domain.report.enums.SubjectType;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ViewReportListResponseDto {

    private Long reportId;
    private String reporter;
    private SubjectType subjectType;
    private String reportedUserId;
    private ReportType reportType;
    private ReportStatus reportStatus;
    private String reportReason;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

}
