package com.sololiving.domain.report.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ViewReportCountsResponseDto {

    private int totalReports;
    private int pendingReports;
    private int resolvedReports;
    private int rejectedReports;

}
