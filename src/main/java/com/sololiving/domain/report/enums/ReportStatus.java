package com.sololiving.domain.report.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReportStatus {

    PENDING("대기중"),
    RESOLVED("처리완료"),
    REJECTED("반려");

    private final String description;

}
