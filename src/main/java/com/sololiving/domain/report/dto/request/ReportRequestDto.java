package com.sololiving.domain.report.dto.request;

import com.sololiving.domain.report.enums.ReportStatus;
import com.sololiving.domain.report.enums.ReportType;
import com.sololiving.domain.report.enums.SubjectType;

import lombok.Getter;

@Getter
public class ReportRequestDto {

    private String reporter;
    private SubjectType subjectType; // Request 필요(필)
    private Long subjectId; // Request 필요(필)
    private ReportType reportType; // Request 필요(필)
    private String reportReason; // Request 필요(불필요)
    private ReportStatus reportStatus;

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

}
