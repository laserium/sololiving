package com.sololiving.domain.report.dto.request;

import com.sololiving.domain.report.enums.ReportStatus;
import com.sololiving.domain.report.enums.ReportType;
import com.sololiving.domain.report.enums.SubjectType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ViewReportRequestDto {

    private ReportType reportType;
    private ReportStatus reportStatus;
    private SubjectType subjectType;
    private String searchUserId;

}
