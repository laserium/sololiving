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
    List<ViewMediaInArticleResponseDto> selectByArticleId(@Param("articleId") Long articleId);

    // 게시글 수정 - 미디어 파일 조회
    List<String> selectMediaUrlsByArticleId(@Param("articleId") Long articleId);

    // 게시글 수정 - Article ID로 미디어 파일 삭제
    void deleteMediaUrlsByArticleId(Long articleId);

    // 게시글 수정 - Media name으로 미디어 파일 삭제
    void deleteMediaByName(String mediaName);
}
