package com.sololiving.domain.report.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sololiving.domain.report.enums.ReportStatus;
import com.sololiving.domain.report.enums.SubjectType;
import com.sololiving.domain.report.vo.ReportVo;

@Mapper
public interface ReportMapper {

    // 신고하기
    void insertReport(ReportVo reportVo);

    // 신고 삭제
    void deleteReport(@Param("reportId") Long reportId);

    // 특정 신고 유형의 신고기록 개수 조회
    int countReport(@Param("subjectId") Long subjectId);

    // 자동 블라인드 처리
    void updateSubjectStatusToBlind(
            @Param("subjectType") SubjectType subjectType,
            @Param("subjectId") Long subjectId);

    // 신고 기록 유무 확인
    boolean existsReport(@Param("reportId") Long reportId);

    // 신고 상태 수정
    void updateReportStatus(
            @Param("reportId") Long reportId,
            @Param("reportStatus") ReportStatus reportStatus);

    ReportStatus selectReportStatusByReportId(@Param("reportId") Long reportId);

}
