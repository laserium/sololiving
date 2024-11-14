package com.sololiving.domain.report.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReportType {
    INAPPROPRIATE_CONTENT("게시판 성격에 부적절함"),
    SPAM("도배"),
    IMPERSONATION_OR_FRAUD("유출/사칭/사기"),
    ABUSE("욕설/비하"),
    SEXUAL_CONTENT("성적인 발언 및 불건전한 만남 및 대화"),
    LEGAL_ISSUE("법적인 문제"),
    HARMFUL_ACTIVITY("유해하거나 위험한 행동");

    private final String description;

}