package com.sololiving.domain.media.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sololiving.domain.media.dto.response.ViewMediaInArticleResponseDto;
import com.sololiving.domain.media.vo.MediaVo;

@Mapper
public interface MediaMapper {
    void insertMedia(MediaVo mediaVo);

    boolean existsByArticleId(@Param("articleId") Long articleId);

    // 게시글 상세 조회 - 미디어 파일 조회
    List<ViewMediaInArticleResponseDto> findByArticleId(@Param("articleId") Long articleId);
}
