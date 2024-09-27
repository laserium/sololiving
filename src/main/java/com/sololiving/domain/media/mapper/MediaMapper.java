package com.sololiving.domain.media.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sololiving.domain.media.vo.MediaVo;

@Mapper
public interface MediaMapper {
    void insertMedia(MediaVo mediaVo);

    boolean existsByArticleId(@Param("articleId") Long articleId);

}
