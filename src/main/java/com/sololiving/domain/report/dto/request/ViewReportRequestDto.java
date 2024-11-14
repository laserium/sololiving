package com.sololiving.domain.report.dto.request;

import com.sololiving.domain.report.enums.ReportStatus;
import com.sololiving.domain.report.enums.ReportType;
import com.sololiving.domain.report.enums.SubjectType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ViewReportRequestDto {

    private ReportType reportType;
    private ReportStatus reportStatus;
    private SubjectType subjectType;
    private String searchUserId;

}
